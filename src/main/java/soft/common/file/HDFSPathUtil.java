package soft.common.file;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import soft.common.StringUtil;

/**
 * 文件名助手
 * 
 * @author fanpei
 *
 */
public class HDFSPathUtil {

	private static String DoubleRoot;
	static {
		DoubleRoot = String.format("%s%s", File.separator, File.separator);
	}

	public static void init() {
	}

	/**
	 * 连接路径
	 * 
	 * @param path1
	 * @param path2
	 * @return
	 */
	public static String combinePathOld(String path1, String path2) {
		String myPath1 = formatDir(path1);
		String myPath2 = formatDir(path2);
		String tmpPath = new StringBuilder().append(myPath1).append(File.separator).append(myPath2).toString();
		return formatDir(tmpPath);
	}

	/**
	 * 连接路径
	 * 
	 * @param path1
	 * @param path2
	 * @return
	 */
	public static String combinePath(String path1, String path2) {

		return new File(path1, path2).getPath();
	}

	/**
	 * 从文件路径中获取文件名-【此文件名为路径原有文件名】
	 * 
	 * @param path /root/hbasekey.123.mp4
	 * @return hbasekey.123.mp4
	 */
	public static String getFileNameFromPath(String path) {
		if (path == null)
			return null;
		if (!path.contains(File.separator))
			return path;

		String[] filnames = path.split(File.separator);
		int length = filnames.length;
		if (length >= 2) {
			return filnames[length - 1];
		}

		return "";
	}

	/**
	 * 获取路径的文件夹路径 并按从外到内排序
	 * 
	 * @param dirPath 文件夹路径
	 * @return
	 */
	public static List<String> getDirPaths(String dirPath, boolean needReverse) {
		List<String> dirs = new LinkedList<>();
		if (!File.separator.equals(dirPath) && dirPath.startsWith(File.separator)) {
			String tmpDir = dirPath;
			while (!StringUtil.isStrNullOrWhiteSpace(tmpDir)) {
				dirs.add(0, tmpDir);
				int index = tmpDir.lastIndexOf(File.separator);
				tmpDir = tmpDir.substring(0, index);
			}
		} else
			dirs.add(dirPath);
		if (needReverse)
			Collections.reverse(dirs);
		return dirs;
	}

	/**
	 * 方法说明：获取当前操作的文件夹名
	 * 
	 * @param path
	 * @return
	 */
	public static String getCurrentDirName(String path) {// 20161226 zgc

		if (!StringUtil.isStrNullOrWhiteSpace(path)) {
			int flagLen = File.separator.length();
			if (File.separator.equals(path)) {
				return path;
			}
			if (path.startsWith(File.separator)) {
				path = path.substring(flagLen, path.length());
			}
			if (path.endsWith(File.separator)) {
				path = path.substring(0, path.length() - flagLen);
			}

			String[] filenames = path.split(File.separator);
			return filenames[filenames.length - 1];
		}
		return File.separator;
	}

	/**
	 * 获取当前路径的最顶级目录
	 * 
	 * @param path
	 * @return
	 */
	public static String getCurrentTopDirName(String path) {
		if (!StringUtil.isStrNullOrWhiteSpace(path)) {
			int flagLen = File.separator.length();
			if (FileIDUtil.isRootDir(path)) {
				return path;
			}
			if (path.startsWith(File.separator)) {
				path = path.substring(flagLen, path.length());
			}
			if (path.endsWith(File.separator)) {
				path = path.substring(0, path.length() - flagLen);
			}

			String[] filenames = path.split(File.separator);
			return filenames[0];
		}
		return File.separator;
	}

	/**
	 * 方法说明：获取当文件夹父文件夹名
	 * 
	 * @param path
	 * @return
	 */
	public static String getFirstDirName(String path) {
		String dirName = "";
		if (!StringUtil.isStrNullOrWhiteSpace(path)) {
			int flagLen = File.separator.length();
			if (FileIDUtil.isRootDir(path)) {
				return "";
			}
			if (path.startsWith(File.separator)) {
				path = path.substring(flagLen, path.length());
			}
			if (path.endsWith(File.separator)) {
				path = path.substring(0, path.length() - flagLen);
			}

			String[] filenames = path.split(File.separator);
			return filenames[0];
		}
		return dirName;

	}

	/**
	 * 获取服务端文件路径
	 * 
	 * @param ParentDir 父路径
	 * @param filename  客户端文件名
	 * @param HBaseKey  HBaseRowkey标识
	 * @return
	 */
	public static String getHdfsPath(String ParentDir, String filename) {
		return combinePath(ParentDir, filename);
	}

	public static String getParentDir(String path) {
		if (StringUtil.isStringNull(path))
			throw new NullPointerException("get the path parent,but path is null");
		File file = new File(path);
		String parent = file.getParent();
		if (StringUtil.isStringNull(parent))
			parent = File.separator;
		else {
			if (parent.contains("\\"))
				parent = parent.replace("\\", "/");
		}
		return parent;
	}

	/**
	 * 从路径中获取父目录
	 * 
	 * @param filepathOrDir
	 * @return
	 */
	public static String getParentDir_old2(String filepathOrDir) {
		StringBuilder newpath = null;
		// 去除首尾文件分隔符
		if (!StringUtil.isStrNullOrWhiteSpace(filepathOrDir)) {
			if (!filepathOrDir.contains(File.separator)) {
			} else {
				if (FileIDUtil.isRootDir(filepathOrDir)) {
				} else {
					newpath = new StringBuilder(filepathOrDir);
					if (filepathOrDir.endsWith(File.separator)) {
						int len = newpath.length();
						newpath.replace(len - 1, len, "");
					}
				}
			}

		}
		return newpath == null ? File.separator : newpath.toString();

	}

	/**
	 * 格式化字符串，去除尾部文件分隔符
	 * 
	 * @param dir 原字符串
	 * @return 修饰和格式化后的字符串
	 */
	public static String formatDir(String dir) {
		if (!StringUtil.isStrNullOrWhiteSpace(dir)) {
			dir = dir.replace("\\", File.separator);
			dir = dir.replace("///", File.separator);
			dir = dir.replace("//", File.separator);
			if (!FileIDUtil.isRootDir(dir)) {
				if (dir.endsWith(File.separator)) {
					dir = dir.substring(0, dir.length() - 1);
				}
				if (!dir.startsWith(File.separator)) {
					dir = new StringBuilder(File.separator).append(dir).toString();
				}
			}
		} else
			throw new NullPointerException("format dir.but dir is null");
		return dir;
	}

	/**
	 * 检测路径是否具有根节点 /,是否有双//
	 * 
	 * @param dir
	 * @return
	 * @throws Exception
	 */
	public static void checkPath(String dir) throws Exception {
		if (!StringUtil.isStrNullOrWhiteSpace(dir) && dir.startsWith(File.separator) && !dir.contains(DoubleRoot))
			return;
		throw new Exception("错误的路径格式，路径为空或无根节点起始符号");
	}

}
