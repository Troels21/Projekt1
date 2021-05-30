package com.company;

public class Brugergraenseflade {

    //set min/max methoder, med kald af java swing og konvertering til double
    public double setmax() {
        String str = javax.swing.JOptionPane.showInputDialog("indtast en maximalstemperatur");
        return Double.parseDouble(str);
    }

    public double setmin() {
        String str = javax.swing.JOptionPane.showInputDialog("indtast en minimumstemperatur");
        return Double.parseDouble(str);
    }

    // bruger interaktion
    static void notifikation(double temperatur) {
        javax.swing.JOptionPane.showConfirmDialog(null, "ALARM patienten er i fare," +
                "temperaturen er " + String.format("%.2f", temperatur) + " celcius");
    }

    // tilsvarende set methode for urgent
    public double SetUrgent() {
        String str = javax.swing.JOptionPane.showInputDialog("indtast en alarm tolerance");
        return Double.parseDouble(str);
    }


}
