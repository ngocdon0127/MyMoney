package com.example.ngocdon.mymoney;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by NgocDon on 9/15/2015.
 */
public class Transaction {

    public static final int INIT = -1;
    public static final int MOBILE_PAID = 0;
    public static final int ATM_WITHDRAW = 1;
    public static final int VISA_WITHDRAW = 2;
    public static final int ATM_RECHARGE = 3;
    public static final int VISA_RECHARGE = 4;
    public static final int ATM_MONTHLY_FEE = 5;
    public static final int VISA_MONTHLY_FEE = 6;
    public static final int ATM_ONLINE_PAID = 7;
    public static final int VISA_ONLINE_PAID = 8;
    public static final int INCREASE = 9;
    public static final int DECREASE = 10;
    public static final int BORROW = 11;
    public static final int PAY_BACK = 12;
    public static final int RESET = 13;

    public static final int MOBILE_PAID_VIA_THANG_UNCLE = 0;
    public static final int MOBILE_PAID_VIA_ATM = 1;
    public static final int LOCAL_POS = 0;
    public static final int BROAD_POS = 1;

    public static final int TYPE_MOBILE_PAID = 0;
    public static final int TYPE_WITHDRAW = 1;
    public static final int TYPE_RECHARGE = 2;
    public static final int TYPE_MONTHLY_FEE = 3;
    public static final int TYPE_ONLINE_PAID = 4;
    public static final int TYPE_INCREASE = 5;
    public static final int TYPE_DECREASE = 6;
    public static final int TYPE_BORROW = 7;
    public static final int TYPE_PAY_BACK = 8;
    public static final int TYPE_RESET_ALL = 9;

    private int type;
    private long money;
    private Date date;
    private String details;
    private int target;
    private int position;

    private long wallet;
    private long atm;
    private long visa;
    private long debt;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        if (money < 0)
            return;
        this.money = money;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDetail() {
        return details;
    }

    public void setDetail(String details) {
        this.details = details;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public long getWallet() {
        return wallet;
    }

    public void setWallet(long wallet) {
        this.wallet = wallet;
    }

    public long getAtm() {
        return atm;
    }

    public void setAtm(long atm) {
        if (atm < 0)
            return;
        this.atm = atm;
    }

    public long getVisa() {
        return visa;
    }

    public void setVisa(long visa) {
        if (visa < 0)
            return;
        this.visa = visa;
    }

    public long getDebt() {
        return debt;
    }

    public void setDebt(long debt) {
        if (debt < 0)
            return;
        this.debt = debt;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Transaction(int type, long money, Date date, String details, int target, int position, long wallet, long atm, long visa, long debt) {
        this.type = type;
        this.money = money;
        this.date = date;
        this.details = details;
        this.target = target;
        this.position = position;
        this.wallet = wallet;
        this.atm = atm;
        this.visa = visa;
        this.debt = debt;
    }

    public void applyTransaction(){
        int type = this.getType();
        long money = this.getMoney();
        switch (type){
            case Transaction.ATM_RECHARGE:
                this.setAtm(this.getAtm() + money);
                this.setWallet(this.getWallet() - money);
                return;
            case Transaction.VISA_RECHARGE:
                this.setVisa(this.getVisa() + money);
                this.setWallet(this.getWallet() - money);
                return;
            case Transaction.ATM_MONTHLY_FEE:
                this.setAtm(this.getAtm() - money);
                return;
            case Transaction.VISA_MONTHLY_FEE:
                this.setVisa(this.getVisa() - money);
                return;
            case Transaction.ATM_ONLINE_PAID:
                this.setAtm(this.getAtm() - money);
                return;
            case Transaction.VISA_ONLINE_PAID:
                this.setVisa(this.getVisa() - money);
                return;
            case Transaction.INCREASE:
                this.setWallet(this.getWallet() + money);
                return;
            case Transaction.DECREASE:
                this.setWallet(this.getWallet() - money);
                return;
            case Transaction.BORROW:
                this.setWallet(this.getWallet() + money);
                this.setDebt(this.getDebt() + money);
                return;
            case Transaction.PAY_BACK:
                this.setWallet(this.getWallet() - money);
                this.setDebt(this.getDebt() - money);
                return;
            case Transaction.MOBILE_PAID:
                if (this.getPosition() == Transaction.MOBILE_PAID_VIA_ATM){
                    this.setAtm(this.getAtm() - (long) (this.getMoney() * Information.MOBILE_PAID_RATE));
                }
                else if (this.getPosition() == Transaction.MOBILE_PAID_VIA_THANG_UNCLE){
                    this.setDebt(this.getDebt() + this.getMoney());
                }
                return;
            case Transaction.ATM_WITHDRAW:
                if (this.getPosition() == Transaction.LOCAL_POS){
                    this.setAtm(this.getAtm() - this.getMoney() - Information.ATM_WITHDRAW_LOCAL_POS_EXTRA);
                }
                else if (this.getPosition() == Transaction.BROAD_POS){
                    this.setAtm(this.getAtm() - this.getMoney() - Information.ATM_WITHDRAW_BROAD_POS_EXTRA);
                }
                this.setWallet(this.getWallet() + this.getMoney());
                return;
            case Transaction.VISA_WITHDRAW:
                if (this.getPosition() == Transaction.LOCAL_POS){
                    this.setVisa(this.getVisa() - this.getMoney() - Information.VISA_WITHDRAW_LOCAL_POS_EXTRA);
                }
                else if (this.getPosition() == Transaction.BROAD_POS){
                    this.setVisa(this.getVisa() - this.getMoney() - Information.VISA_WITHDRAW_BROAD_POS_EXTRA);
                }
                this.setWallet(this.getWallet() + this.getMoney());
                return;
        }
    }

    public long countAvailable(){
        return this.getWallet() + ((this.getAtm() >= 100000) ? (this.getAtm() - 100000) : 0) + ((this.getVisa() >= 100000) ? (this.getVisa() - 100000) : 0) - this.getDebt();
    }

    public static String format(long l){
        DecimalFormat df = new DecimalFormat("#,###,###,###");
        String result = df.format(l);
        return result;
    }

    public String getDateString(){
        Date d = this.getDate();
        return d.getDate() + "/" + (d.getMonth() + 1) + "/" + (d.getYear() + 1900);
    }

    public String getTimeString(){
        Date d = this.getDate();
        return this.getDateString() + " " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds();
    }

    @Override
    public String toString() {
        String s = "Time: " + this.getTimeString();
        s += "\nMoney: " + Transaction.format(this.getMoney());
        s += "\nDetails: " + this.getDetail();
        s += "\nWith: " + Information.PEOPLE[this.getTarget()];
        return s;
    }
}
