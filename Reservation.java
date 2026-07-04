
package gaming;



public class Reservation {
    private int idReservation;
    private int itemId;
    private String startTime;
    private int hDuration;      // مدة اللعب بالساعات
    private double ratePerH;    // سعر الساعة عند الحجز
    private double totalCost;   // التكلفة الإجمالية المحسوبة

    public Reservation(int idReservation, int itemId, String startTime, int hDuration, double ratePerH) {
        this.idReservation = idReservation;
        this.itemId = itemId;
        this.startTime = startTime;
        this.hDuration = hDuration;
        this.ratePerH = ratePerH;
        this.calculateTotalCost(); // حساب الإجمالي تلقائياً عند إنشاء الحجز
    }

    private void calculateTotalCost() {
        this.totalCost = this.hDuration * this.ratePerH;
    }

    // دوال الكبسلة (Getters & Setters)
    public int getIdReservation() { return idReservation; }
    public void setIdReservation(int idReservation) { this.idReservation = idReservation; }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public int getHDuration() { return hDuration; }
    public void setHDuration(int hDuration) { 
        this.hDuration = hDuration; 
        calculateTotalCost(); // إعادة حساب الإجمالي في حال تعديل المدة
    }

    public double getRatePerH() { return ratePerH; }
    public void setRatePerH(double ratePerH) { 
        this.ratePerH = ratePerH; 
        calculateTotalCost(); // إعادة حساب الإجمالي في حال تعديل السعر
    }

    public double getTotalCost() { return totalCost; }
}
