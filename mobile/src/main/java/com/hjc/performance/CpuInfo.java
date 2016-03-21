/*
 * Copyright (c) 2012-2013 NetEase, Inc. and other contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.hjc.performance;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.hjc.scripttool.R;
import com.hjc.service.PerformanceService;
import com.hjc.util.Constants;
import com.hjc.util.ProcessInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;


public class CpuInfo {

	private Context context;
	private long processCpu, totalCpu;
	private ArrayList<Long> idleCpu = new ArrayList();
	private boolean isInitialStatics = true;
	private SimpleDateFormat formatterFile;
	private MemoryInfo mi;
	private ArrayList<String> cpuUsedRatio = new ArrayList();
	private long processCpu2, totalCpu2;
	private ArrayList<Long> idleCpu2 = new ArrayList();
	private String processCpuRatio = "";
	private ArrayList<String> totalCpuRatio = new ArrayList();
	private int pid;

	private static final String INTEL_CPU_NAME = "model name";
	private static final String CPU_DIR_PATH = "/sys/devices/system/cpu/";
	private static final String CPU_X86 = "x86";
	private static final String CPU_INFO_PATH = "/proc/cpuinfo";
	private static final String CPU_STAT = "/proc/stat";

	public CpuInfo(Context context, int pid) {
		this.pid = pid;
		this.context = context;
		formatterFile = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		cpuUsedRatio = new ArrayList();
	}

	/**
	 * read the status of CPU.
	 * 
	 * @throws FileNotFoundException
	 */
	public void readCpuStat() throws IOException {
		String processPid = Integer.toString(pid);
		String cpuStatPath = "/proc/" + processPid + "/stat";
		try {
			// monitor cpu stat of certain process
			RandomAccessFile processCpuInfo = new RandomAccessFile(cpuStatPath, "r");
			String line = "";
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.setLength(0);
			while ((line = processCpuInfo.readLine()) != null) {
				stringBuffer.append(line + "\n");
			}
			String[] tok = stringBuffer.toString().split(" ");
			processCpu = Long.parseLong(tok[13]) + Long.parseLong(tok[14]);

			processCpuInfo.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			e.printStackTrace();
		}
		readTotalCpuStat();
	}

	/**
	 * read stat of each CPU cores
	 */
	private void readTotalCpuStat() throws IOException {
//		try {
//			// monitor total and idle cpu stat of certain process
//			RandomAccessFile cpuInfo = new RandomAccessFile(CPU_STAT, "r");
//			String line = "";
//			while ((null != (line = cpuInfo.readLine())) && line.startsWith("cpu ")) {
//
//				String[] toks = line.split("\\s+");
//				idleCpu.add(Long.parseLong(toks[4]));
//				long totalCpuF = Long.parseLong(toks[1]) + Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
//						+ Long.parseLong(toks[6]) + Long.parseLong(toks[5]) + Long.parseLong(toks[7]);
//
//
//				Log.e(Constants.TAG, processCpu + ", " + totalCpuF);
//			}
//
//			cpuInfo.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		Process p = Runtime.getRuntime().exec("cat " + CPU_STAT + " |grep cpu ");
		BufferedReader buff = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = "";
		while ((line = buff.readLine()) != null) {
			if(line.contains("cpu ")){
				String[] toks = line.split("\\s+");
				totalCpu = Long.parseLong(toks[1]) + Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
						+ Long.parseLong(toks[6]) + Long.parseLong(toks[5]) + Long.parseLong(toks[7]);

			}
		}

	}

	/**
	 * get CPU name.
	 * 
	 * @return CPU name
	 */
	public String getCpuName() {
		try {
			RandomAccessFile cpuStat = new RandomAccessFile(CPU_INFO_PATH, "r");
			// check cpu type
			if (Build.CPU_ABI.equalsIgnoreCase(CPU_X86)) {
				String line;
				while (null != (line = cpuStat.readLine())) {
					String[] values = line.split(":");
					if (values[0].contains(INTEL_CPU_NAME)) {
						cpuStat.close();
						return values[1];
					}
				}
			} else {
				String[] cpu = cpuStat.readLine().split(":"); // cpu信息的前一段是含有processor字符串，此处替换为不显示
				cpuStat.close();
				return cpu[1];
			}
		} catch (IOException e) {

		}
		return "";
	}



	/**
	 * display directories naming with "cpu*"
	 * 
	 * @author andrewleo
	 */
	class CpuFilter implements FileFilter {
		@Override
		public boolean accept(File pathname) {
			// Check if filename matchs "cpu[0-9]"
			if (Pattern.matches("cpu[0-9]", pathname.getName())) {
				return true;
			}
			return false;
		}
	}

	/**
	 * get CPU core numbers
	 * 
	 * @return cpu core numbers
	 */
	public int getCpuNum() {
		try {
			// Get directory containing CPU info
			File dir = new File(CPU_DIR_PATH);
			// Filter to only list the devices we care about
			File[] files = dir.listFiles(new CpuFilter());
			return files.length;
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}

	/**
	 * get CPU core list
	 * 
	 * @return cpu core list
	 */
	public ArrayList<String> getCpuList() {
		ArrayList<String> cpuList = new ArrayList<String>();
		try {
			// Get directory containing CPU info
			File dir = new File(CPU_DIR_PATH);
			// Filter to only list the devices we care about
			File[] files = dir.listFiles(new CpuFilter());
			for (int i = 0; i < files.length; i++) {
				cpuList.add(files[i].getName());
			}
			return cpuList;
		} catch (Exception e) {
			e.printStackTrace();
			cpuList.add("cpu0");
			return cpuList;
		}
	}

	/**
	 * reserve used ratio of process CPU and total CPU, meanwhile collect
	 * network traffic.
	 * 
	 * @return network traffic ,used ratio of process CPU and total CPU in
	 *         certain interval
	 */
	public String getCpuRatioInfo() throws IOException {
		DecimalFormat fomart = new DecimalFormat();
		fomart.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
		fomart.setGroupingUsed(false);
		fomart.setMaximumFractionDigits(2);
		fomart.setMinimumFractionDigits(2);

		cpuUsedRatio.clear();
		idleCpu.clear();
		totalCpu = 0;
		totalCpuRatio.clear();
		readCpuStat();
		StringBuffer totalCpuBuffer = new StringBuffer();

		if (0 != totalCpu2) {
			long s_aC = processCpu - processCpu2;
			long s_aT = totalCpu - totalCpu2;
			if(s_aC > 0 && s_aT > 0){
				processCpuRatio = fomart.format(100 * ((double) (s_aC) / ((double) (s_aT)))) + Constants.PCT;
			}
			else {
				processCpuRatio = Constants.NA;
			}


		} else {
			processCpuRatio = "0%";
			totalCpu2 = totalCpu;
			processCpu2 = processCpu;
		}

		if (isPositive(processCpuRatio) && totalCpu > 0) {
			processCpu2 = processCpu;
			totalCpu2 =  totalCpu;

		}
		return processCpuRatio;
	}

	/**
	 * is text a positive number
	 * 
	 * @param text
	 * @return
	 */
	private boolean isPositive(String text) {
		Double num;
		try {
			num = Double.parseDouble(text);
		} catch (NumberFormatException e) {
			return false;
		}
		return num >= 0;
	}

}
