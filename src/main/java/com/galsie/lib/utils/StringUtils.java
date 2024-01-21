package com.galsie.lib.utils;

import java.util.function.Function;

public class StringUtils {

    /**
     * Compares versions in the form of ##.##.##.##......-WHATEVER
     * Returns a positive integer if version1 > version2
     * <p>
     * Algo:
     * For each array
     * - Split by '-' to have ["##.##.##", "WHATEVER"]
     * - Split the first part by '.' to  have versions ["##", "##", ##"]
     * - Parse versions to integers to be [##, ##, ##]
     * Compare first part (versions):
     * - Compare integer versions first. If one of the arrays is shorter, consider the versions as 0
     * - If the integer versions at the same index are equal, continue.
     * - If they are not, returns version1 - version2 at that index (positive if version1 > version 2)
     * <p>
     * Compare second part:
     * - If any doesn't have the second part, consider it an empty string (noting that an empty string is always considering lower in version in the string compareTo method)
     * - Compare the strings using compareTo
     *
     *
     * Note: See CompareVersionsests
     */
    public static int compareVersions(String version1, String version2) {
        var version1DescSplit = version1.split("-");
        var version2DescSplit = version2.split("-");
        var splitVersion1 = version1DescSplit[0].split("\\.");
        var splitVersion2 = version2DescSplit[0].split("\\.");
        var longest = Math.max(splitVersion2.length, splitVersion1.length);
        for (int i = 0; i < longest; i++) {
            var v1I = splitVersion1.length <= i ? 0 : Integer.parseInt(splitVersion1[i]);
            var v2I = splitVersion2.length <= i ? 0 : Integer.parseInt(splitVersion2[i]);
            if (v1I - v2I == 0) {
                continue;
            }
            return v1I - v2I;
        }
        var v1Desc = version1DescSplit.length > 1 ? version1DescSplit[1] : "";
        var v2Desc = version2DescSplit.length > 1 ? version2DescSplit[1] : "";
        return v1Desc.compareTo(v2Desc);
    }


    /**
     * Joins the paths with a forward slash
     * @param paths
     * @return
     */
    public static String joinPaths(String... paths){
        var builder = new StringBuilder();
        for (String path: paths){
            if (!builder.isEmpty() && !path.startsWith("/") && !builder.toString().endsWith("/")){
                builder.append("/");
            }
            builder.append(path);
        }
        var str = builder.toString();
        while (str.endsWith("/")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static String joinPathsWithStart(String startPath, String... paths){
        var builder = new StringBuilder();
        builder.append(startPath);
        for (String path: paths){
            if (!builder.isEmpty() && !path.startsWith("/") && !builder.toString().endsWith("/")){
                builder.append("/");
            }
            builder.append(path);
        }
        var str = builder.toString();
        while (str.endsWith("/")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static String randomAlphanumeric(int length){
        final var chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        var builder = new StringBuilder();
        for (int i = 0; i< length; i++){
            var randomIndex = (int) (Math.random()*chars.length);
            builder.append(chars[randomIndex]);
        }
        return builder.toString();
    }

    public static String randomAlphanumeric(int length, Function<Integer, Integer> randomSupplier){
        final var chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        var builder = new StringBuilder();
        for (int i = 0; i< length; i++){
            var randomIndex = randomSupplier.apply(chars.length);
            builder.append(chars[randomIndex]);
        }
        return builder.toString();
    }



}
