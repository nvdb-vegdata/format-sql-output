package no.vegvesen.nvdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FormatLines {

    private static final int MAXCHAR = 100;

    public static void main(String [] args) {
        FormatLines w = new FormatLines();
        w.run();
    }

    public void run() {

        Scanner scanner = new Scanner(System.in);
        for (;;) {
            System.out.print("\nLim inn resultatet fra oracle : ");
            String line = scanner.nextLine();
            Formatter formatter = new Formatter();
            System.out.println(formatter.format(line));
            System.out.println("---------------------");
        }
    }

    class Formatter {

        private String formattedString = "";
        private int indentLevel = 0;

        private String format(String s) {

            s = s.replaceAll("\\s", "");
            if (s.length() == 0) {
                return formattedString;
            }
            if (s.startsWith(",")) {
                formattedString += ", ";
                return format(s.substring(1));
            } else if (s.startsWith(")")) {
                indentLevel--;
                formattedString += "\n" + printIndent(indentLevel) + ")";
                return format(s.substring(1));
            } else {
                int paranStart = s.indexOf("(");
                int paranEnd = s.indexOf(")");
                if ((hasBothEndAndStartParan(s) && paranEnd > MAXCHAR && paranStart < MAXCHAR)) {
                    String front = s.substring(0, paranStart + 1);
                    formattedString += "\n" + printIndent(indentLevel) + front;
                    indentLevel += calcIndentChange(front);
                    return format(s.substring(paranStart + 1));
                } else if (hasBothEndAndStartParan(s) && paranEnd < MAXCHAR && paranStart < MAXCHAR){
                    int end = eatBiggestPossibleLines(s);
                    String front = s.substring(0, end);
                    formattedString += "\n" + printIndent(indentLevel) + front;
                    indentLevel += calcIndentChange(front);
                    return format(s.substring(end));
                } else if (hasEndParantes(s) && paranEnd < MAXCHAR) {
                    String front = s.substring(0, paranEnd + 1);
                    formattedString += "\n" + printIndent(indentLevel) + front;
                    indentLevel += calcIndentChange(front);
                    return format(s.substring(paranEnd + 1));
                } else {
                    int max = (s.length() < MAXCHAR) ? s.length() : MAXCHAR;
                    int commaInd = s.substring(0, max).lastIndexOf(",");
                    int end = (commaInd == -1) ? max : commaInd;
                    String front = s.substring(0, end);
                    formattedString += "\n" + printIndent(indentLevel) + front;
                    indentLevel += calcIndentChange(front);
                    return format(s.substring(end));
                }
            }
        }

        private boolean hasStartParantes(String s) {
            return s.contains("(");
        }

        private boolean hasEndParantes(String s) {
            return s.contains(")");
        }

        private boolean hasBothEndAndStartParan(String s) {
            return  s.contains("(") && s.contains(")");
        }

        private int eatBiggestPossibleLines(String s) {
            List<Integer> rightParan = new ArrayList<>();
            List<Integer> leftParan = new ArrayList<>();
            int max = (s.length() < MAXCHAR) ? s.length() : MAXCHAR;
            for (int i = 0; i < max; i++) {
                if (s.charAt(i) == '(') leftParan.add(i);
                if (s.charAt(i) == ')') rightParan.add(i);
            }
            int minMatchingParan = Math.min(rightParan.size(), leftParan.size()) - 1;
            return rightParan.get(minMatchingParan) + 1;
        }

        private int calcIndentChange(String s) {
            int start = 0;
            int end = 0;
            for (int i = 0; i < s.length(); i ++) {
                if (s.charAt(i) == '(') start++;
                if (s.charAt(i) == ')') end++;
            }
            return start - end;
        }

        private String printIndent(int indentLevel) {
            String indent = "";
            for (int i = 0; i < indentLevel; i++) {
                indent += "  ";
            }
            return indent;
        }
    }
}
