package xyz.ersut.core.util.verify;

import cn.hutool.core.util.ObjectUtil;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 对象工具类
 */
public class ObjectUtils extends ObjectUtil {

	public static boolean isNull(List<?> list) {
		return null == list || list.size() == 0;
	}

	public static boolean isNull(Set<?> set) {
		return null == set || set.size() == 0;
	}

	public static boolean isNull(Map<?, ?> map) {
		return null == map || map.size() == 0;
	}

	public static boolean isNull(Long lg) {
		return null == lg || lg.longValue() == 0L;
	}

	public static boolean isNull(Integer it) {
		return null == it || it.intValue() == 0;
	}

	public static boolean isNull(File file) {
		return null == file || !file.exists();
	}

	public static boolean isNull(Object[] strs) {
		return null == strs || strs.length == 0;
	}

	public static boolean isNotNull(List<?> list) {
		return !isNull((List)list);
	}

	public static boolean isNotNull(Set<?> set) {
		return !isNull((Set)set);
	}

	public static boolean isNotNull(Map<?, ?> map) {
		return !isNull((Map)map);
	}

	public static boolean isNotNull(Long lg) {
		return !isNull(lg);
	}

	public static boolean isNotNull(Integer it) {
		return !isNull(it);
	}

	public static boolean isNotNull(File file) {
		return !isNull(file);
	}

	public static boolean isNotNull(Object[] strs) {
		return !isNull(strs);
	}
}