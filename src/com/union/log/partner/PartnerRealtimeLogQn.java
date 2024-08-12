package com.union.log.partner;

import com.union.util.CommUtils;
import com.union.util.LogFileSplitUtil;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PartnerRealtimeLogQn {
	
	private static final String LOG_USMS_PATTERN = "^([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) (-|[0-9]*) (\".*?\") ([^ ]*) ([^ ]*) ([^ ]*) (\".*\") ([^ ]*)";
	private static final String LOG_AC_PATTERN = "^(\\d{14}) ([^ ]*) ([^ ]*) ([^ ]*) \"([^\"]*)\" ([^ ]*) ([^ ]*) \"([^\"]*)\" \"([^\"]*)\" ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*)";
	private static final Pattern USMS_PATTERN = Pattern.compile(LOG_USMS_PATTERN);
	private static final Pattern AC_PATTERN = Pattern.compile(LOG_AC_PATTERN);

	public static String handleRealtimeLog(List<String> domainList,Map<String,String> keyWordsRuleList, String logForSpark){
		String resultMsg = "success: no files!";
		String logPath = "./";

		File directory = new File(logPath);
		if (directory.listFiles() != null){
			for (File file : directory.listFiles()) {

				String fileName = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("/") + 1);

				if (!fileName.endsWith(".gz"))
					continue;
				
				resultMsg = handleRealtimeLogFile(file, domainList,keyWordsRuleList,logForSpark);
			}
		}

		return resultMsg;
	}
	
	public static String handleRealtimeLogFile(File file, List<String> domainList, Map<String, String> keyWordsRuleList, String logForSpark){

		String unzipFilePathName = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("."));
		String fileName = unzipFilePathName.substring(unzipFilePathName.lastIndexOf("/") + 1);

		String[] fileInfo = fileName.split("_");
		if (fileInfo.length != 2 || fileInfo[1].length() < 8) {
			return "File: " + file.getAbsolutePath() + " name is wrong format!";
		}
		String datetime = fileInfo[1];
		
		if (com.union.util.GZipUtils.decompress(file, false, null)) {

			String bakGzipFileDir = "PATH /" + datetime.substring(0, 8);

			File fileDir = new File(bakGzipFileDir);
			fileDir.mkdirs();

			file.renameTo(new File(bakGzipFileDir + "/" + file.getName()));

			if (fileName.startsWith("plugin.log")) {
				
				System.out.println("--Handle plugin log file :" + fileName);				
				String resultMsg =  LogFileSplitUtil.readLogAndSplitData(unzipFilePathName, domainList, keyWordsRuleList);
				if(!"success".equals(resultMsg)) return resultMsg;
				
			} else {
				
				System.out.println("--Handle access log file :" + fileName);
				String resultMsg = handleQuRealtimeAccessLog(unzipFilePathName, domainList,logForSpark);
				if(!"success".equals(resultMsg)) return resultMsg;
			}

			File unzipFile = new File(unzipFilePathName);
			unzipFile.delete();
			
			return "success";
		} else {
			file.renameTo(new File(file.getAbsolutePath() + ".error"));
			File unzipFile = new File(unzipFilePathName);
			if (!unzipFile.delete())
				System.out.println("Delete file: " + unzipFilePathName + " failed!");
			return "Decompress file error: " + file.getAbsolutePath();
		}
	}
	
	public static String handleQuRealtimeAccessLog(String unzipFilePathName, List<String> domainList, String logForSpark) {
		
		FileInputStream fis = null;
		InputStreamReader read = null;
		File unzipFile = null;
		

		try {
			unzipFile = new File(unzipFilePathName);
			fis = new FileInputStream(unzipFile);
			read = new InputStreamReader(fis, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(read, 400 * 1024);
	
			String[] fileInfo = unzipFile.getName().split("_");
			if (fileInfo.length != 2 || fileInfo[1].length() < 8) {
				return "File: " + unzipFile.getAbsolutePath() + " name is wrong format!";
			}
			String lineLog = null;
			while ((lineLog = bufferedReader.readLine()) != null) {
	
				String domain = "";
				Matcher usmsLog = USMS_PATTERN.matcher(lineLog);
				if (usmsLog.find()) {
					if(usmsLog.group(9).indexOf("://") < 0){		
						continue;
					}else{
						domain = usmsLog.group(9).substring(usmsLog.group(9).indexOf("://") + 3,usmsLog.group(9).indexOf("/", usmsLog.group(9).indexOf("://") + 3));
					}
				}else{
					Matcher acLog = AC_PATTERN.matcher(lineLog);
					if(acLog.find()){
						

						StringBuffer usmsFmLog = new StringBuffer();
						usmsFmLog.append(acLog.group(16)).append(" ");
						usmsFmLog.append(CommUtils.convertDateTimeToMillisInt(acLog.group(1))).append(" ");
						usmsFmLog.append(acLog.group(8)).append(" ");
						usmsFmLog.append(CommUtils.convertDateTimeToHour(acLog.group(1))).append(" ");
						usmsFmLog.append(acLog.group(17));
						
					}else{
						System.out.println("log format is wrong ! logling: " + lineLog);
					}
				}
			}

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
	
	
}
