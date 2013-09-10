/** 
 
Copyright 2013 Intel Corporation, All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 
*/ 

package com.intel.cosbench.controller.archiver;

import java.io.*;
import java.util.Scanner;

import com.intel.cosbench.config.*;
import com.intel.cosbench.config.castor.CastorConfigTools;
import com.intel.cosbench.exporter.*;
import com.intel.cosbench.log.*;
import com.intel.cosbench.model.*;

/**
 * This class encapsulates operations to archive workload results, the archive
 * folder is at "$CURRENT/archive".
 * 
 * @author ywang19, qzheng7
 * 
 */
public class SimpleWorkloadArchiver implements WorkloadArchiver {

    private static final Logger LOGGER = LogFactory.getSystemLogger();

    private static final File ROOT_DIR = new File("archive");

    static {
        if (!ROOT_DIR.exists()) {
            if(!ROOT_DIR.mkdirs())
            	LOGGER.warn(ROOT_DIR + " doesn't create as expected.");
        }
        String path = ROOT_DIR.getAbsolutePath();
        LOGGER.info("using {} for storing workload archives", path);
    }

    public SimpleWorkloadArchiver() {
        /* empty */
    }

    @Override
    public synchronized void archive(WorkloadInfo info) {
        File runDir = new File(ROOT_DIR, getRunDirName(info));
        try {
            doArchive(info, runDir);
        } catch (Exception e) {
            LOGGER.error("fail to archive workload", e);
            return;
        } finally {
            try {
                updateCount();
            } catch (Exception e) {
                LOGGER.error("cannot update workload count", e);
            }
        }
        String id = info.getId();
        LOGGER.info("workload {} has been successfully archived", id);
    }

    private void doArchive(WorkloadInfo info, File runDir) throws IOException {
        exportWorkloadRun(info);
        if(!runDir.mkdir())
        	LOGGER.warn(runDir + " doesn't create as expected.");
        exportWorkload(info, runDir);
        exportLatency(info, runDir);
        for (StageInfo sInfo : info.getStageInfos())
            exportStage(sInfo, runDir);
        exportConfig(info.getWorkload(), runDir);
        exportLog(info, runDir);
        exportPerformanceMatrix(info);
    }

    private static String getRunDirName(WorkloadInfo info) {
        String name = info.getId();
        name += '-' + info.getWorkload().getName();
        return name;
    }

    private void exportWorkloadRun(WorkloadInfo info) throws IOException {
        File file = new File(ROOT_DIR, "run-history.csv");
        boolean ready = file.exists() && file.length() > 0;
        Writer writer = new BufferedWriter(new FileWriter(file, true));
        RunExporter exporter = Exporters.newRunExporter(info);
        try {
            if (!ready)
                exporter.init(writer);
            exporter.export(writer);
        } finally {
            writer.close();
        }
        String id = info.getId();
        String path = file.getAbsolutePath();
        String msg = "run item for workload {} has been added to {}";
        LOGGER.debug(msg, id, path);
    }

    private void exportWorkload(WorkloadInfo info, File parent)
            throws IOException {
        File file = new File(parent, getWorkloadFileName(info) + ".csv");
        Writer writer = new BufferedWriter(new FileWriter(file));
        WorkloadExporter exporter = Exporters.newWorkloadExporter(info);
        try {
            exporter.export(writer);
        } finally {
            writer.close();
        }
        String id = info.getId();
        String path = file.getAbsolutePath();
        String msg = "perf details of workload {} has been exported to {}";
        LOGGER.debug(msg, id, path);
    }

    private static String getWorkloadFileName(WorkloadInfo info) {
        String name = info.getId();
        name += "-" + info.getWorkload().getName();
        return name;
    }

    private void exportLatency(WorkloadInfo info, File parent)
            throws IOException {
        File file = new File(parent, getLatencyFileName(info) + ".csv");
        Writer writer = new BufferedWriter(new FileWriter(file));
        LatencyExporter exporter = Exporters.newLatencyExporter(info);
        try {
            exporter.export(writer);
        } finally {
            writer.close();
        }
        String id = info.getId();
        String path = file.getAbsolutePath();
        String msg = "latency details of workload {} has been exported to {}";
        LOGGER.debug(msg, id, path);
    }

    private static String getLatencyFileName(WorkloadInfo info) {
        String name = info.getId();
        name += "-" + info.getWorkload().getName();
        name += "-rt-histogram";
        return name;
    }

    private void exportStage(StageInfo info, File parent) throws IOException {
        File file = new File(parent, getStageFileName(info) + ".csv");
        Writer writer = new BufferedWriter(new FileWriter(file));
        StageExporter exporter = Exporters.newStageExporter(info);
        try {
            exporter.export(writer);
        } finally {
            writer.close();
        }
        String id = info.getId();
        String path = file.getAbsolutePath();
        String msg = "perf details of stage {} has been exported to {}";
        LOGGER.debug(msg, id, path);
    }

    private static String getStageFileName(StageInfo info) {
        String name = info.getId();
        name += "-" + info.getStage().getName();
        return name;
    }

    private void exportConfig(Workload workload, File parent)
            throws IOException {
        File file = new File(parent, "workload-config.xml");
        Writer writer = new BufferedWriter(new FileWriter(file));
        WorkloadWriter ww = CastorConfigTools.getWorkloadWriter();
        try {
            writer.write(ww.toXmlString(workload));
        } finally {
            writer.close();
        }
        String name = workload.getName();
        String path = file.getAbsolutePath();
        String msg = "config xml for workload {} has been generated at {}";
        LOGGER.debug(msg, name, path);
    }

    @Override
    public File getWorkloadConfig(WorkloadInfo info) {
        File runDir = new File(ROOT_DIR, getRunDirName(info));
        return new File(runDir, "workload-config.xml");
    }

    private void exportLog(WorkloadInfo info, File parent) throws IOException {
        File file = new File(parent, "workload.log");
        Writer writer = new BufferedWriter(new FileWriter(file));
        LogExporter exporter = Exporters.newLogExporter(info);
        try {
            exporter.export(writer);
        } finally {
            writer.close();
        }
        String id = info.getId();
        String path = file.getAbsolutePath();
        String msg = "driver logs for workload {} has been merged at {}";
        LOGGER.debug(msg, id, path);
    }

    @Override
    public File getWorkloadLog(WorkloadInfo info) {
        File runDir = new File(ROOT_DIR, getRunDirName(info));
        return new File(runDir, "workload.log");
    }

    private void exportPerformanceMatrix(WorkloadInfo info) throws IOException {
        File file = new File(ROOT_DIR, "workloads.csv");
        boolean ready = file.exists() && file.length() > 0;
        Writer writer = new BufferedWriter(new FileWriter(file, true));
        MatrixExporter exporter = Exporters.newMatrixExporter(info);
        try {
            if (!ready)
                exporter.init(writer);
            exporter.export(writer);
        } finally {
            writer.close();
        }
        String id = info.getId();
        String path = file.getAbsolutePath();
        String msg = "perf details of workload {} has been added to {}";
        LOGGER.debug(msg, id, path);
    }

    private void updateCount() throws IOException {
        int count = 0;
        File file = new File(ROOT_DIR, ".meta");
        if (file.exists() && file.length() > 0) {
            Reader reader = new BufferedReader(new FileReader(file));
            try {
                count = new Scanner(reader).nextInt();
            } finally {
                reader.close();
            }
        }
        Writer writer = new BufferedWriter(new FileWriter(file));
        try {
            writer.write(String.valueOf(++count));
        } finally {
            writer.close();
        }
        LOGGER.debug("workload count has been updated as {}", count);
    }

    @Override
    public synchronized int getTotalWorkloads() {
        int count = 0;
        try {
            count = retrieveCount();
        } catch (Exception e) {
            LOGGER.error("fail to retrieve workload count", e);
        }
        return count;
    }

    private int retrieveCount() throws IOException {
        File file = new File(ROOT_DIR, ".meta");
        if (!file.exists() || file.length() == 0)
            return 0;
        int count;
        Reader reader = new BufferedReader(new FileReader(file));
        try {
            count = new Scanner(reader).nextInt();
        } finally {
            reader.close();
        }
        LOGGER.debug("workload count has been retrieved as {}", count);
        return count;
    }

}
