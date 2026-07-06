package gaming;

/**
 * الكلاس المسؤول عن إدارة عمليات الحجز والألعاب داخل الصالة.
 * يقوم بإنشاء الحجوزات، ربطها بالأجهزة، وحساب التكلفة الإجمالية تلقائياً
 * بناءً على مدة اللعب وسعر الساعة.
 */
public class Reservation {

    /** المعرف الفريد لعملية الحجز (Reservation ID) */
    private int idReservation;

    /** الرقم التعريفي للجهاز المحجوز المرتبط بـ {@link GameItem} */
    private int itemId;

    /** وقت بداية الحجز أو اللعب (مثال: "2026-07-06 18:00") */
    private String startTime;

    /** مدة اللعب المحجوزة محسوبة بالساعات */
    private int hDuration;

    /** سعر الساعة المعتمد للجهاز وقت إجراء هذا الحجز */
    private double ratePerH;

    /** التكلفة الإجمالية للحجز ويتم حسابها برمجياً وتلقائياً */
    private double totalCost;

    /**
     * ينشئ حجزاً جديداً ويقوم بحساب التكلفة الإجمالية فوراً عند إنشاء الكائن.
     * * @param idReservation المعرف الفريد للحجز.
     * @param itemId رقم الجهاز المراد حجزه.
     * @param startTime وقت بدء الحجز.
     * @param hDuration عدد ساعات اللعب.
     * @param ratePerH سعر الساعة للجهاز.
     */
    public Reservation(int idReservation, int itemId, String startTime, int hDuration, double ratePerH) {
        this.idReservation = idReservation;
        this.itemId = itemId;
        this.startTime = startTime;
        this.hDuration = hDuration;
        this.ratePerH = ratePerH;
        this.calculateTotalCost();
    }

    /**
     * ميثود داخلية (Private) تقوم بحساب التكلفة الإجمالية عبر ضرب 
     * عدد الساعات في سعر الساعة الواحد.
     */
    private void calculateTotalCost() {
        this.totalCost = this.hDuration * this.ratePerH;
    }

    /**
     * استرجاع الرقم التعريفي للحجز.
     * @return رقم الحجز الحالي.
     */
    public int getIdReservation() { return idReservation; }

    /**
     * تحديث الرقم التعريفي للحجز.
     * @param idReservation رقم الحجز الجديد.
     */
    public void setIdReservation(int idReservation) { this.idReservation = idReservation; }

    /**
     * استرجاع رقم الجهاز المرتبط بالحجز.
     * @return رقم الجهاز (Item ID).
     */
    public int getItemId() { return itemId; }

    /**
     * ربط الحجز بجهاز آخر عبر رقمه التعريفي.
     * @param itemId رقم الجهاز الجديد.
     */
    public void setItemId(int itemId) { this.itemId = itemId; }

    /**
     * استرجاع وقت بداية الحجز.
     * @return نص يمثل وقت البداية.
     */
    public String getStartTime() { return startTime; }

    /**
     * تحديث وقت بداية الحجز.
     * @param startTime وقت البداية الجديد.
     */
    public void setStartTime(String startTime) { this.startTime = startTime; }

    /**
     * استرجاع مدة الحجز بالساعات.
     * @return عدد الساعات المحجوزة.
     */
    public int getHDuration() { return hDuration; }

    /**
     * تحديث مدة الحجز بالساعات، مع إعادة حساب التكلفة الإجمالية تلقائياً.
     * @param hDuration عدد الساعات الجديد.
     */
    public void setHDuration(int hDuration) {
        this.hDuration = hDuration;
        calculateTotalCost();
    }

    /**
     * استرجاع سعر الساعة المعتمد في هذا الحجز.
     * @return سعر الساعة الحالي.
     */
    public double getRatePerH() { return ratePerH; }

    /**
     * تحديث سعر الساعة في الحجز، مع إعادة حساب التكلفة الإجمالية تلقائياً.
     * @param ratePerH سعر الساعة الجديد.
     */
    public void setRatePerH(double ratePerH) {
        this.ratePerH = ratePerH;
        calculateTotalCost();
    }

    /**
     * استرجاع التكلفة الإجمالية المحسوبة للحجز.
     * @return القيمة المالية الإجمالية للحجز.
     */
    public double getTotalCost() { return totalCost; }
}