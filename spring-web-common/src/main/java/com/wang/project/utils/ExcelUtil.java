package com.wang.project.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import com.wang.project.annations.ClassToExcle;
import com.wang.project.annations.FieldToExcel;

/**
 * Excel工具类
 * <dl>
 * <dt>1.0版本存在的问题</dt>
 * <dd>①注解与非注解不能共存</dd>
 * <dd>②不支持非注解功能</dd>
 * <dd>③集合和映射类型的成员不支持</dd>
 * <dd>④新增支持值映射</dd>
 * <dd>⑤新增支持前缀和后缀</dd>
 * <dd>⑥新增替换功能</dd>
 * </dl>
 * 
 * @author wyw
 * @data 2018年4月2日
 * @version 1.0
 */
public class ExcelUtil {

	protected static int MAX_ROW_PER_SHEET = 10000;

	/**
	 * 创建标题行<br>
	 * 
	 * @param clz
	 * @param row
	 * @param cellStyle
	 * @param offset
	 * @since 2.0
	 */
	protected static void createHeadRowWithAnnotation(Class<?> clz, HSSFRow row, CellStyle cellStyle, int offset) {
		boolean success = verifyClass(clz);
		if (!success) {
			return;
		}
		// 获取所有的成员域
		Field[] fields = clz.getDeclaredFields();
		for (Field field : fields) {
			// 获取指定注解
			FieldToExcel fieldToExcel = field.getAnnotation(FieldToExcel.class);
			if (fieldToExcel == null) {
				continue;
			}

			if (StringUtil.isNotBlank(fieldToExcel.name())) {
				HSSFCell cell = row.createCell(fieldToExcel.column() + offset);
				cell.setCellStyle(cellStyle);
				HSSFRichTextString text = new HSSFRichTextString(fieldToExcel.name());
				cell.setCellValue(text);
			} else {
				int clzOffset = fieldToExcel.offset();
				Class<?> fieldClz = field.getType();
				createHeadRowWithAnnotation(fieldClz, row, cellStyle, clzOffset);
			}
		}
	}

	/**
	 * 创建数据行
	 * 
	 * @param object
	 * @param sheet
	 * @param cellStyle
	 * @param offset
	 * @param row
	 * @since 2.0
	 */
	protected static void createdDataRowWithAnnotation(Object object, HSSFRow row, HSSFCellStyle cellStyle,
			int offset) {
		if (object == null) {
			return;
		}
		boolean success = verifyClass(object.getClass());
		if (!success) {
			return;
		}
		// 获取所有的成员域
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			// 获取指定注解
			FieldToExcel fieldToExcel = field.getAnnotation(FieldToExcel.class);
			if (fieldToExcel == null) {
				continue;
			}
			try {
				// 获取属性名
				String fieldName = field.getName();
				// 获取方法名
				String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				// 获取方法
				Method method = object.getClass().getMethod(methodName);
				// 获取值
				Object value = method.invoke(object);
				if (StringUtil.isNotBlank(fieldToExcel.name())) {
					// 新增功能，支持值映射
					value = mapping(fieldToExcel, value);
					// 新增功能，支持替换
					value = replace(fieldToExcel, value);
					// 新增功能，支持前缀和后缀
					value = addfix(fieldToExcel, value);
					HSSFCell cell = row.createCell(fieldToExcel.column() + offset);
					cell.setCellStyle(cellStyle);
					HSSFRichTextString text = new HSSFRichTextString(value.toString());
					cell.setCellValue(text);
				} else {
					// 获取偏移量
					int clzOffset = fieldToExcel.offset();
					createdDataRowWithAnnotation(value, row, cellStyle, clzOffset);
				}

			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 增进替换功能
	 * 
	 * @param fieldToExcel
	 * @param value
	 * @return
	 * @since 2.1
	 */
	protected static Object replace(FieldToExcel fieldToExcel, Object value) {
		String[] srcvalue = fieldToExcel.value();
		String[] replace = fieldToExcel.replace();
		boolean replaceAll = fieldToExcel.replaceAll();
		int[] index = fieldToExcel.index();
		if (srcvalue.length >= 0 && replace.length >= 0) {
			for (int i = 0; i < Math.min(srcvalue.length, replace.length); i++) {
				if (value != null) {
					if (replaceAll) {
						value = value.toString().replaceAll(srcvalue[i], replace[i]);
					} else {
						int begin = value.toString().indexOf(srcvalue[i]);
						int length = srcvalue[i].length();
						for (int j = 0; j < index.length; j++) {
							int number = 0;// 计算指定值出现的次数
							while (begin >= 0) {// 原始值中包含指定值
								number++;
								if (number == index[j]) {// 满足条件，开始替换值
									String prefix = value.toString().substring(0, begin);
									String subfix = value.toString().substring(begin + length);
									value = prefix + replace[i] + subfix;
									break;
								} else {
									begin = value.toString().indexOf(srcvalue[i], begin + 1);
								}
							}
						}
					}
				}
			}
		}
		return value;
	}

	/**
	 * 增加前缀和后缀
	 * 
	 * @param fieldToExcel
	 * @param value
	 * @return
	 * @since 2.1
	 */
	protected static Object addfix(FieldToExcel fieldToExcel, Object value) {
		String prefix = fieldToExcel.prefix();
		if (StringUtil.isNotBlank(prefix)) {
			value = prefix + value;
		}
		String suffix = fieldToExcel.suffix();
		if (StringUtil.isNotBlank(suffix)) {
			value = value + suffix;
		}
		return value;
	}

	/**
	 * 增加映射功能
	 * 
	 * @param fieldToExcel
	 * @param value
	 * @return
	 * @since 2.1
	 */
	protected static Object mapping(FieldToExcel fieldToExcel, Object value) {
		String[] srcvalue = fieldToExcel.value();
		String[] valueMapping = fieldToExcel.mapping();
		if (srcvalue.length >= 0 && valueMapping.length >= 0) {
			for (int i = 0; i < Math.min(srcvalue.length, valueMapping.length); i++) {
				if (value != null && value.toString().equals(srcvalue[i])) {
					value = valueMapping[i];
					break;
				}
			}
		}
		return value;
	}

	/**
	 * 校验
	 * 
	 * @param clz
	 * @return
	 * @since 2.0
	 */
	protected synchronized static boolean verifyClass(Class<?> clz) {
		// 判断对象的类是否是Collection
		boolean belongCollection = SerializableUtil.belongCollection(clz);
		if (belongCollection) {
			return false;
		}
		// 判断对象的类是否是Map
		boolean belongMap = SerializableUtil.belongMap(clz);
		if (belongMap) {
			return false;
		}
		// 获取该类上的指定注解
		ClassToExcle classToExcel = clz.getAnnotation(ClassToExcle.class);
		if (classToExcel == null) {
			return false;
		}
		return true;
	}

	/**
	 * 导出数据到Excel表中<br>
	 * 
	 * @param list
	 * @param sheetName
	 * @param heads
	 * @return
	 * @since 2.0
	 */
	public static HSSFWorkbook writeDataToExcel(List<?> list, String sheetName, List<String> heads) {
		// 判断集合是否为空
		if (list == null || list.isEmpty()) {
			return null;
		}
		// 创建工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();

		int totality = list.size();
		int pageCount = totality / MAX_ROW_PER_SHEET;
		for (int i = 0; i < pageCount; i++) {

		}
		// 创建工作表
		if (StringUtil.isBlank(sheetName)) {
			String name = list.get(0).getClass().getName();
			int index = name.lastIndexOf(".");
			String className = name.substring(index + 1);
			sheetName = className;
		}
		HSSFSheet sheet = workbook.createSheet(sheetName);

		// 定义单元格样式
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.LEFT);// 指定单元格水平左对齐
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 指定单元格垂直居中
		cellStyle.setWrapText(true);// 设置单元格自动换行

		// 单元格字体
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);// 字体大小
		font.setFontName("宋体");
		font.setBold(true);

		// 字体应用到样式里面
		cellStyle.setFont(font);

		// 创建标题行
		HSSFRow row = sheet.createRow(0);
		if (heads != null && !heads.isEmpty()) {
			createHeadRowWithList(heads, row, cellStyle);
		} else {
			createHeadRowWithAnnotation(list.get(0).getClass(), row, cellStyle, 0);
		}

		// 创建数据行
		int rowNum = 1;
		for (Object object : list) {
			row = sheet.createRow(rowNum);
			createdDataRowWithAnnotation(object, row, cellStyle, 0);
			rowNum++;
		}
		return workbook;
	}

	/**
	 * 导出数据到Excel表中<br>
	 * 
	 * @param list
	 * @return
	 * @since 2.0
	 */
	public static HSSFWorkbook writeDataToExcel(List<?> list) {
		return writeDataToExcel(list, null, null);
	}

	/**
	 * 创建标题行
	 * 
	 * @param heads
	 * @param row
	 * @param cellStyle
	 * @since 1.0
	 */
	private static void createHeadRowWithList(List<String> heads, HSSFRow row, HSSFCellStyle cellStyle) {
		for (short i = 0; i < heads.size(); i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(cellStyle);
			HSSFRichTextString text = new HSSFRichTextString(heads.get(i));
			cell.setCellValue(text);
		}
	}

}
