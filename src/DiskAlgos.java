//Name: Richard, Seth
//        Project: PA-3 (Disk Scheduling Algorithm)
//        File: DiskAlgos.java
//        Instructor: Feng Chen
//        Class: cs4103-sp20
//        LogonID: cs410380


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class DiskAlgos {
    public static void main(String[] args) {
        int diskHeadPosition = Integer.parseInt(args[0]);
        int blockTotal = Integer.parseInt(args[1]);
        String algorithm = args[2];
        String filePath = args[3];
        File req = new File(filePath);
        try {
            int legth = (int) Files.lines(Paths.get(filePath)).count();
            int[] requests = new int[legth];

            Scanner fin = new Scanner(new File(filePath));
            int count = 0;
            while (fin.hasNextLine()) {
                requests[count] = Integer.parseInt(fin.nextLine());
                count++;
            }


            switch (algorithm) {
                case "FCFS":
                    System.out.println("Processing with FCFS:\n");
                    FCFSProcess(diskHeadPosition, blockTotal, requests);
                    break;
                case "SSTF":
                    System.out.println("Processing with SSFT:\n");
                    SSFTProcess(diskHeadPosition, blockTotal, requests);
                    break;
                case "SCAN":
                    System.out.println("Processing with SCAN:\n");
                    SCANProcess(diskHeadPosition, blockTotal, requests);
                    break;
                default:
                    System.out.println("Please run again with a correct scheduling algorithm.");
                    return;
            }
        }
        catch (Exception e) {
            System.err.print(e);
        }
    }

    public static void FCFSProcess(int diskHeadPosition, int blockTotal, int[] requests) {
        int distance = 0;
        int targetPos = 0;
        int currentPos = diskHeadPosition;
        double transferTime = 0.031;
        double seekTime;
        double rotLatency;
        double accessTime;

        // Used after program completes
        int totalDiskRequests = 0;
        int totalHeadMovement = 0;
        double totalSeekTime = 0;
        double totalRotLatency = 0;
        double totalTransferTime = 0;
        double totalAccessTime = 0;
        for (int i = 0; i < requests.length; i++) {
            targetPos = requests[i];
            distance = Math.abs(currentPos - targetPos);
            seekTime = Math.abs(targetPos - currentPos) * 0.1;
            rotLatency = Math.random() * 8.3;
            accessTime = seekTime + rotLatency + transferTime;

            System.out.println("Target disk position: " + targetPos);
            System.out.println("Current disk position: " + currentPos);
            System.out.println("Distance between current and target position: " + distance);
            System.out.println("Seek time: " + seekTime);
            System.out.println("Rotational latency: " + rotLatency);
            System.out.println("Transfer time: " + transferTime);
            System.out.println("Access time: " + accessTime + "\n");

            totalSeekTime += seekTime;
            totalHeadMovement += distance;
            totalDiskRequests++;
            totalRotLatency += rotLatency;
            totalTransferTime += transferTime;
            totalAccessTime += accessTime;

            currentPos = targetPos;
        }
        System.out.println("Total number of disk requests: " + totalDiskRequests);
        System.out.println("Total amount of disk head movement: " + totalHeadMovement);
        System.out.println("Total amount of seek time: " + totalSeekTime);
        System.out.println("Total amount of rotational latency: " + totalRotLatency);
        System.out.println("Total amount of transfer time: " + totalTransferTime);
        System.out.println("Total amount of access time: " + totalAccessTime);
    }

    public static void SSFTProcess(int diskHeadPosition, int blockTotal, int[] requests) {
        int distance = 0;
        int targetPos = 0;
        int currentPos = diskHeadPosition;
        double transferTime = 0.031;
        double seekTime;
        double rotLatency;
        double accessTime;

        // Used after program completes
        int totalDiskRequests = 0;
        int totalHeadMovement = 0;
        double totalSeekTime = 0;
        double totalRotLatency = 0;
        double totalTransferTime = 0;
        double totalAccessTime = 0;
        for (int i = 0; i < requests.length; i++) {
            int min = Integer.MAX_VALUE;
            int minIndex = 0;
            for (int j = 0; j < requests.length; j++) {
                if (requests[j] != -1) {
                    int minTest = Math.abs(currentPos - requests[j]);
                    if (minTest < min) {
                        min = minTest;
                        minIndex = j;
                    }
                }
            }

            targetPos = requests[minIndex];
            requests[minIndex] = -1;
            distance = Math.abs(currentPos - targetPos);
            seekTime = Math.abs(targetPos - currentPos) * 0.1;
            rotLatency = Math.random() * 8.3;
            accessTime = seekTime + rotLatency + transferTime;

            System.out.println("Target disk position: " + targetPos);
            System.out.println("Current disk position: " + currentPos);
            System.out.println("Distance between current and target position: " + distance);
            System.out.println("Seek time: " + seekTime);
            System.out.println("Rotational latency: " + rotLatency);
            System.out.println("Transfer time: " + transferTime);
            System.out.println("Access time: " + accessTime + "\n");

            totalSeekTime += seekTime;
            totalHeadMovement += distance;
            totalDiskRequests++;
            totalRotLatency += rotLatency;
            totalTransferTime += transferTime;
            totalAccessTime += accessTime;

            currentPos = targetPos;
        }
        System.out.println("Total number of disk requests: " + totalDiskRequests);
        System.out.println("Total amount of disk head movement: " + totalHeadMovement);
        System.out.println("Total amount of seek time: " + totalSeekTime);
        System.out.println("Total amount of rotational latency: " + totalRotLatency);
        System.out.println("Total amount of transfer time: " + totalTransferTime);
        System.out.println("Total amount of access time: " + totalAccessTime);
    }

    public static void SCANProcess(int diskHeadPosition, int blockTotal, int[] requests) {
        Arrays.sort(requests);
        int currentPos = diskHeadPosition;

        int targetPosIndex = Arrays.binarySearch(requests, currentPos) * -1 - 1;

        targetPosIndex = (requests[targetPosIndex] > currentPos) ? targetPosIndex - 1 : targetPosIndex;

        int distance = 0;
        double transferTime = 0.031;
        double seekTime;
        double rotLatency;
        double accessTime;

        // Used after program completes
        int totalDiskRequests = 0;
        int totalHeadMovement = 0;
        double totalSeekTime = 0;
        double totalRotLatency = 0;
        double totalTransferTime = 0;
        double totalAccessTime = 0;
        while (targetPosIndex > -1) {
            distance = Math.abs(currentPos - requests[targetPosIndex]);
            seekTime = Math.abs(requests[targetPosIndex] - currentPos) * 0.1;
            rotLatency = Math.random() * 8.3;
            accessTime = seekTime + rotLatency + transferTime;

            System.out.println("Target disk position: " + requests[targetPosIndex]);
            System.out.println("Current disk position: " + currentPos);
            System.out.println("Distance between current and target position: " + distance);
            System.out.println("Seek time: " + seekTime);
            System.out.println("Rotational latency: " + rotLatency);
            System.out.println("Transfer time: " + transferTime);
            System.out.println("Access time: " + accessTime + "\n");

            totalSeekTime += seekTime;
            totalHeadMovement += distance;
            totalDiskRequests++;
            totalRotLatency += rotLatency;
            totalTransferTime += transferTime;
            totalAccessTime += accessTime;

            currentPos = requests[targetPosIndex];
            requests[targetPosIndex] = -1;
            targetPosIndex--;
        }

        for (int i = 0; i < requests.length; i++) {
            if (requests[i] == -1) {
                i++;
            }
            else {
                targetPosIndex = i;
                break;
            }
        }

        while (targetPosIndex < requests.length) {
            distance = Math.abs(currentPos - requests[targetPosIndex]);
            seekTime = distance * 0.1;
            rotLatency = Math.random() * 8.3;
            accessTime = seekTime + rotLatency + transferTime;

            System.out.println("Target disk position: " + requests[targetPosIndex]);
            System.out.println("Current disk position: " + currentPos);
            System.out.println("Distance between current and target position: " + distance);
            System.out.println("Seek time: " + seekTime);
            System.out.println("Rotational latency: " + rotLatency);
            System.out.println("Transfer time: " + transferTime);
            System.out.println("Access time: " + accessTime + "\n");

            totalSeekTime += seekTime;
            totalHeadMovement += distance;
            totalDiskRequests++;
            totalRotLatency += rotLatency;
            totalTransferTime += transferTime;
            totalAccessTime += accessTime;

            currentPos = requests[targetPosIndex];
            targetPosIndex++;
        }

        System.out.println("Total number of disk requests: " + totalDiskRequests);
        System.out.println("Total amount of disk head movement: " + totalHeadMovement);
        System.out.println("Total amount of seek time: " + totalSeekTime);
        System.out.println("Total amount of rotational latency: " + totalRotLatency);
        System.out.println("Total amount of transfer time: " + totalTransferTime);
        System.out.println("Total amount of access time: " + totalAccessTime);
    }
}
