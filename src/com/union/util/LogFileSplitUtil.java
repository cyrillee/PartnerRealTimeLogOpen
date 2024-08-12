package com.union.util;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogFileSplitUtil {

	public static String readLogAndSplitData(String unzipFilePathName, List<String> domainList, Map<String,String> keyWordsRuleList) {
		
		Map<Integer, List<String>> splitedLogTxtMap = new HashMap<Integer, List<String>>();
		
		splitedLogTxtMap.put(0, new ArrayList<String>());
		splitedLogTxtMap.put(1, new ArrayList<String>());
		splitedLogTxtMap.put(2, new ArrayList<String>());		
		splitedLogTxtMap.put(3, new ArrayList<String>());
		
		FileInputStream fis = null;
		InputStreamReader read = null;
		File unzipFile = null;
		try {
			unzipFile = new File(unzipFilePathName);
			fis = new FileInputStream(unzipFile);
			read = new InputStreamReader(fis, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(read, 400 * 1024);
	
			String lineLog = null;
			while ((lineLog = bufferedReader.readLine()) != null) {
	
				Pattern p = Pattern.compile("\\s+");
				Matcher m = p.matcher(lineLog);
				lineLog= m.replaceAll(" ");
				
				String[] details = lineLog.split(" ");
				String domain = details[5];
				String logsType = details[0];
				
				int marketingChannel = -1;
				for (String domainMarketing : domainList) {
					if (domainMarketing.startsWith("*")) {
						String[] tmpDM = domainMarketing.split("_");
						if (tmpDM != null && tmpDM.length == 2 && !tmpDM[1].equals("") && CommUtils.isInteger(tmpDM[1])) {
							String genClasses = tmpDM[0].substring(2);
							if (domain.endsWith(genClasses))
								marketingChannel = Integer.parseInt(tmpDM[1]);
						}
					} else {
						if (domainMarketing.startsWith(domain + "_")) {
							String[] tmp = domainMarketing.split("_");
							if (tmp != null && tmp.length == 2 && !tmp[1].equals("") && CommUtils.isInteger(tmp[1]))
								marketingChannel = Integer.parseInt(tmp[1]);
						}
					}
				}
				splitedLogTxtMap.get(marketingChannel).add(lineLog);
			}
			convertLogs(splitedLogTxtMap, unzipFile.getName(),keyWordsRuleList);
			
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "readLogAndSplitData error!";
		} finally{
			try {
				read.close();
				fis.close();
				unzipFile.deleteOnExit();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void makeSplitedFiles(Map<Integer, List<String>> splitedLogTxtMap,String fileName) throws Exception {
		String[] fileNameSplit = fileName.split("_");
	}
	
	private static final String LOG_ENTRY_PATTERN = "([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*)";
	private static final Pattern PATTERN = Pattern.compile(LOG_ENTRY_PATTERN);

	public static void convertLogs(Map<Integer, List<String>> splitedLogTxtMap,String fileName,Map<String,String> keyWordsRuleList)throws Exception {
		
		makeSplitedFiles(splitedLogTxtMap, fileName);
	}

	public static void outputGZFile(String filename, Collection objs) throws Exception {

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File(""),true));
			if (objs != null && objs.size() > 0)
				for (Iterator iterator = objs.iterator(); iterator.hasNext();) {
					bw.append(iterator.next().toString());
					bw.newLine();
				}
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("outputGZFile error");
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String appendFileToFile(String writeFilePath, String readFilePath)  {

		// 声明输入输出流
		FileInputStream readFIS = null;
		FileOutputStream writeFOS = null;
		try {
			File readFile = new File(readFilePath);
			readFIS = new FileInputStream(readFile);
			// 创建byte数组
			Long readFilelength = readFile.length();
			byte[] readFileContent = new byte[readFilelength.intValue()];  
			readFIS.read(readFileContent);

			// 要写入的文件路径
			File writeFile = new File(writeFilePath);
			if (!writeFile.exists()) 
				writeFile.createNewFile();
			
			writeFOS = new FileOutputStream(writeFile, true);
			writeFOS.write(readFileContent, 0, (int) readFile.length());
			
			readFile = null;
			writeFile = null;
			
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "AppendFileToFile is error! " + writeFilePath + " " + readFilePath;
		} finally {
			try {
				readFIS.close();
				writeFOS.close();
			} catch (Exception e) {
				e.printStackTrace();
				return "AppendFileToFile close FIS OR FOS error!";
			}
		}

	}

	public static List<String> parseLineLog(String lingLog, List<String> detailList) {

		String splitChar = lingLog.startsWith("\"") ? "\"" : " ";

		lingLog = CommUtils.leftTrimQuote(lingLog);
		String[] tmpArray = lingLog.split(splitChar, 2);

		detailList.add(tmpArray[0]);
		if (tmpArray.length == 2) {
			String rightSide = CommUtils.leftTrim(tmpArray[1]);
			if (!rightSide.equals(""))
				parseLineLog(rightSide, detailList);
		}
		return detailList;
	}

}
