package com.ccnode.codegenerator.dialog;

import com.ccnode.codegenerator.dialog.dto.mybatis.ClassMapperMethod;
import com.ccnode.codegenerator.dialog.dto.mybatis.ColumnAndField;
import com.ccnode.codegenerator.dialog.dto.mybatis.MapperMethodEnum;
import com.ccnode.codegenerator.util.GenCodeUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bruce.ge on 2016/12/29.
 */
public class MapperUtil {

    public static final String SELECT = "select";
    public static final String WHERE = "where";

    @Nullable
    static String generateSql(List<GenCodeProp> newAddedProps, List<ColumnAndField> deleteFields, String sqlText, List<ColumnAndField> existingFields) {
        String beforeWhere = sqlText;
        //text before where make it on it.
        int start = 0;
        int end = sqlText.length();
        String lowerSqlText = sqlText.toLowerCase();
        int where = findMatchFor(lowerSqlText, WHERE);
        if (where != -1) {
            end = where;
            beforeWhere = sqlText.substring(0, where);
        }
        int select = findMatchFor(lowerSqlText, SELECT);
        if (select != -1) {
            start = select + SELECT.length();
            beforeWhere = beforeWhere.substring(select + SELECT.length());
        }

        //not support for with select function ect.
        if (beforeWhere.contains("(")) {
            return null;
        }
        String[] split = beforeWhere.split(",");

        List<String> beforeFormatted = new ArrayList<>();
        for (String uu : split) {
            String term = trimUseLess(uu);
            boolean isDeleted = false;
            for (ColumnAndField deleteField : deleteFields) {
                if (term.toLowerCase().equals(deleteField.getColumn().toLowerCase())) {
                    isDeleted = true;
                    break;
                }
            }
            if (isDeleted) {
                continue;
            }
            beforeFormatted.add(uu);
        }
        String beforeInsert = "";
        for (int i = 0; i < beforeFormatted.size(); i++) {
            beforeInsert += beforeFormatted.get(i);
            if (i != beforeFormatted.size() - 1) {
                beforeInsert += ",";
            }
        }

        String newAddInsert = "";

        for (int i = 0; i < newAddedProps.size(); i++) {
            newAddInsert += ",";
            newAddInsert += GenCodeUtil.wrapComma(newAddedProps.get(i).getColumnName());
            newAddInsert += "\n" + GenCodeUtil.TWO_RETRACT;
        }

        String newValueText = sqlText.substring(0, start) + beforeInsert + newAddInsert + sqlText.substring(end);
        return newValueText;
    }

    private static int findMatchFor(String lowerSqlText, String where) {
        Pattern matcher = Pattern.compile("\\b" + where + "\\b");
        Matcher matcher1 = matcher.matcher(lowerSqlText);
        if (matcher1.find()) {
            return matcher1.start();
        } else {
            return -1;
        }
    }

    private static String trimUseLess(String uu) {
        int len = uu.length();
        int start = 0;
        int end = uu.length();
        char c;
        c = uu.charAt(start++);
        while (start != len && (c == '\n') || (c == ' ') || c == '\t' || c == '`') {
            c = uu.charAt(start++);
        }
        if (start == len) {
            return "";
        }
        c = uu.charAt(--end);
        while (end >= start && (c == '\n') || (c == ' ') || c == '\t' || c == '`') {
            c = uu.charAt(--end);
        }
        return uu.substring(start - 1, end + 1);
    }

    public static String generateMapperMethod(List<GenCodeProp> newAddedProps, List<ColumnAndField> deletedFields, MapperMethodEnum type, ClassMapperMethod classMapperMethod, String sqlText) {
        switch (type) {
            case INSERT: {

                break;
            }
        }

        return null;
    }
}