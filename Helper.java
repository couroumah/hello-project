package com.devops.toolbox.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static com.devops.toolbox.util.StandardConstants.REGEX_INVALID_CHARACTERS;

@Slf4j
@Component
public class Helper {
    DecimalFormat df = new DecimalFormat("###,###,###");

    /**
     * Returns a collection of found patterns from a line and a single pattern
     *
     * @param line    current line
     * @param pattern pattern to search
     * @return a collection of found patterns from a line and a single pattern
     */
    public static String findLineMatchingWithPattern(String line,
                                                     @NonNull String pattern) {
//        log.debug("--start [{}] pattern [{}]...", Thread.currentThread().getStackTrace()[1].getMethodName(), pattern);

        String lineMatchingWithPattern = StringUtils.EMPTY;
        Pattern patternDd = Pattern.compile(pattern);
        Matcher matcher = patternDd.matcher(line);

        while (matcher.find()) {
            String matcherGroup = matcher.group();
            lineMatchingWithPattern = line;
            log.debug("--pattern found [{}] in line: {}", pattern, line);
        }

//        log.debug("--end [{}] pattern [{}]", Thread.currentThread().getStackTrace()[1].getMethodName(), pattern);
        return lineMatchingWithPattern;
    }

    /**
     * Returns a collection of found patterns from a line and a collection of patterns
     * @param line current line
     * @param patterns list of patterns to search
     * @return a collection of found patterns from a line and a collection of patterns
     */

    /**
     * Returns a map with the pattern as key and a list of matching lines
     *
     * @param lines
     * @param pattern
     * @return Map<Key: pattern, Value: list of matching lines>
     */
    public Map<String, List<String>> findLinesMatchingWithPattern(List<String> lines,
                                                                  String pattern) {
        Map<String, List<String>> map = new HashMap<>();
        List<String> matchingLines = new ArrayList<>();
        for (String line : lines) {
            if (!findLineMatchingWithPattern(line, pattern).isEmpty()) {
                matchingLines.add(findLineMatchingWithPattern(line, pattern));
//                log.info("Pattern[{}] found at line: {}", pattern, line);
            }
        }

        map.put(pattern, matchingLines);
        return map;
    }

    /**
     * Returns <code>a map<Filename,List of patterns></code>
     *
     * @param pathInputFile file to process
     * @param patterns      list of patterns to search
     * @param charset       Charset
     * @return <code>a map<Filename,List of patterns></code>
     */
    public Map<String, List<Map<String, List<String>>>> findPatternsInFile(Path pathInputFile,
                                                                           @NonNull List<String> patterns,
                                                                           Charset charset) throws IOException {
        log.debug("--start [{}] patterns #{}...", Thread.currentThread().getStackTrace()[1].getMethodName(), patterns.size());

        Map<String, List<Map<String, List<String>>>> rc = new HashMap<>();
        log.info("--start read file [{}]...", pathInputFile);
        List<String> lines = Files.readAllLines(pathInputFile, charset);
        log.info("--end read file [{}], nb lines [{}]", pathInputFile, df.format(lines.size()));
        List<Map<String, List<String>>> listOfMapOfLinesMatchingWithPattern = new ArrayList<>();

        // Looping across patterns
        for (String pattern : patterns) {
            log.debug("--processing pattern [{}]...", pattern);
            Map<String, List<String>> mapOfLinesMatchingWithPattern = findLinesMatchingWithPattern(lines, pattern);
            boolean hasData = false;
            int counter = 0;

            for (String key : mapOfLinesMatchingWithPattern.keySet()) {
                List<String> lines1 = mapOfLinesMatchingWithPattern.get(key);
                counter += lines1.size();
                hasData = lines1.size() > 0 ? true : false;
            }
            if (hasData) {
                log.info("--found [#{}] matches for pattern [{}]", df.format(counter), pattern);
                listOfMapOfLinesMatchingWithPattern.add(mapOfLinesMatchingWithPattern);
            }

            log.debug("--processed pattern [{}]", pattern);
        }

        if (!listOfMapOfLinesMatchingWithPattern.isEmpty()) {
            rc.put(pathInputFile.toString(), listOfMapOfLinesMatchingWithPattern);
            log.info("--file [{}] total pattern(s) found [{}]", pathInputFile, df.format(listOfMapOfLinesMatchingWithPattern.size()));
        }

        log.debug("--end [{}] patterns #{}", Thread.currentThread().getStackTrace()[1].getMethodName(), patterns.size());
        return rc;
    }

    /**
     * Returns <code>a map<Filename,List of patterns></code>
     *
     * @param pathInputFile file to process
     * @param patterns      list of patterns to search
     * @return <code>a map<Filename,List of patterns></code>
     */
    public Map<String, List<Map<String, List<String>>>> findPatternsInFile(Path pathInputFile,
                                                                           @NonNull List<String> patterns) throws IOException {
        log.debug("--start [{}] patterns #{}...", Thread.currentThread().getStackTrace()[1].getMethodName(), patterns.size());

        log.info("--processing file [{}]...", pathInputFile);
        Map<String, List<Map<String, List<String>>>> rc = new HashMap<>();
        try {
            Map<String, List<Map<String, List<String>>>> map = findPatternsInFile(pathInputFile, patterns, StandardCharsets.UTF_8);
            log.info("--processed file [{}]", pathInputFile);
            return map;
        } catch (IOException e) {
            try {
                return findPatternsInFile(pathInputFile, patterns, Charset.forName("Cp1252"));
            } catch (IOException ex) {
                new RuntimeException("Cannot parse file. IOException: {}" + ex.getMessage());
                return rc;
            }
        }
    }


    public static void toUnzipFromFile(Path pathSource,
                                       Path pathTarget) throws IOException {
        log.debug("--start [{}] pathSource [{}] to pathTarget [{}]...",
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                pathSource,
                pathTarget);

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(pathSource.toFile()))) {

            //list files in zip
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {
                boolean isDirectory = false;
                if (zipEntry.getName().endsWith(File.separator)) {
                    isDirectory = true;
                }

                Path newPath = zipSlipProtect(zipEntry, pathTarget);

                if (isDirectory) {
                    Files.createDirectories(newPath);
                } else {
                    if (newPath.getParent() != null && Files.notExists(newPath.getParent())) {
                        Files.createDirectories(newPath.getParent());
                    }
                }

                Files.copy(zipInputStream, newPath, StandardCopyOption.REPLACE_EXISTING);
                log.debug("Copy file [{}] to [{}]", zipInputStream, newPath);
                zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.closeEntry();
        }//try

        log.debug("--end [{}] pathSource [{}] to pathTarget [{}]",
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                pathSource,
                pathTarget);
    }

    public static void toZipFromDirectory(Path pathInputDirectory,
                                       Path pathOutputZip) throws IOException {
        log.debug("--start [{}] pathInputDirectory [{}] to pathOutputZip [{}]...",
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                pathInputDirectory,
                pathOutputZip);

//        FileOutputStream fos = new FileOutputStream("dirCompressed.zip");
        FileOutputStream fos = new FileOutputStream(pathOutputZip.toFile());
        ZipOutputStream zipOut = new ZipOutputStream(fos);

//        File fileToZip = new File(sourceFile);
        File fileToZip = pathInputDirectory.toFile();
        zipFile(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();


        log.debug("--end [{}] pathSource [{}] to pathOutputZip [{}]",
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                pathInputDirectory,
                pathOutputZip);
    }

    public static void toZipMultipleFiles(File[] srcFiles,
                                          Path pathOutputZip) throws IOException {
        log.info("--start [{}]...", Thread.currentThread().getStackTrace()[1].getMethodName());

        final FileOutputStream fos = new FileOutputStream(pathOutputZip.toFile());
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for (File fileToZip : srcFiles) {
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }

        zipOut.close();
        fos.close();

        log.debug("--end [{}] to pathOutputZip [{}]",
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                pathOutputZip);
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    private static Path zipSlipProtect(ZipEntry zipEntry, Path pathTarget) throws IOException {
        log.debug("--start [{}] zipEntry [{}] to pathTarget [{}]...",
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                zipEntry,
                pathTarget);

        // Test zip slip vulnerability
        // Path targetDirectoryResolved = targetDir.resolve("../../" + zipEntry.getName())
        String zipEntryFilename = zipEntry.getName();
        if (zipEntryFilename.contains("?")) {
            zipEntryFilename = zipEntry.getName().replaceAll(REGEX_INVALID_CHARACTERS, "");
        }

        String zipEntryFilenameNormalized = removeTrailingChars(removeLeadingChars(zipEntryFilename, " "), " ");

        Path targetDirResolved = pathTarget.resolve(zipEntryFilenameNormalized);
        Path normalizePath = targetDirResolved.normalize();

        if (!normalizePath.startsWith(pathTarget)) {
            throw new IOException(("Bad zip entry: [%s]").formatted(zipEntryFilenameNormalized));
        }

        log.debug("--end [{}] zipEntry [{}] to pathTarget [{}]",
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                zipEntry,
                pathTarget);

        return normalizePath;
    }

    public static String removeLeadingChars(String text, String stripChars) {
        return StringUtils.stripStart(text, stripChars);
    }

    public static String removeTrailingChars(String text, String stripChars) {
        return StringUtils.stripEnd(text, stripChars);
    }

}
