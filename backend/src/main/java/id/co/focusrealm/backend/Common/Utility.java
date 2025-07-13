package id.co.focusrealm.backend.Common;

public class Utility {

    public static String generateNextId(String lastId) {
        String prefix = lastId.replaceAll("\\d", "");
        String numberPart = lastId.replaceAll("\\D", "");

        int nextNumber = Integer.parseInt(numberPart) + 1;

        String newNumberPart = String.format("%0" + numberPart.length() + "d", nextNumber);

        return prefix + newNumberPart;
    }

}
