package com.ndurska.coco_client.calendar.monthlysummary;

import java.io.Serializable;
import java.time.LocalDate;

public class MonthSummaryDTO implements Serializable {
    private LocalDate date;
    private int numberOfAppointments;
    private int numberOfAbsentClients;
    private int numberOfClientsWithNoLastPayment;
    private int amountPaidThisMonth;
    private int amountPredicted;

    public MonthSummaryDTO(LocalDate date, int numberOfAppointments, int numberOfAbsentClients, int numberOfClientsWithNoLastPayment, int amountPaidThisMonth, int amountPredicted) {
        this.date = date;
        this.numberOfAppointments = numberOfAppointments;
        this.numberOfAbsentClients = numberOfAbsentClients;
        this.numberOfClientsWithNoLastPayment = numberOfClientsWithNoLastPayment;
        this.amountPaidThisMonth = amountPaidThisMonth;
        this.amountPredicted = amountPredicted;
    }

    public MonthSummaryDTO(){
    }

    public LocalDate getDate() {
        return date;
    }

    public int getNumberOfAppointments() {
        return numberOfAppointments;
    }

    public int getNumberOfAbsentClients() {
        return numberOfAbsentClients;
    }

    public int getNumberOfClientsWithNoLastPayment() {
        return numberOfClientsWithNoLastPayment;
    }

    public int getAmountPaidThisMonth() {
        return amountPaidThisMonth;
    }

    public int getAmountPredicted() {
        return amountPredicted;
    }

    public static final class Builder {
        private LocalDate date;
        private int numberOfClients;
        private int numberOfAbsentClients;
        private int numberOfClientsWithNoLastPayment;
        private int amountPaidThisMonth;
        private int amountPredicted;

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder numberOfClients(int numberOfClients) {
            this.numberOfClients = numberOfClients;
            return this;
        }

        public Builder numberOfAbsentClients(int numberOfAbsentClients) {
            this.numberOfAbsentClients = numberOfAbsentClients;
            return this;
        }

        public Builder numberOfClientsWithNoLastPayment(int numberOfClientsWithNoLastPayment) {
            this.numberOfClientsWithNoLastPayment = numberOfClientsWithNoLastPayment;
            return this;
        }

        public Builder amountPaidThisMonth(int amountPaidThisMonth) {
            this.amountPaidThisMonth = amountPaidThisMonth;
            return this;
        }

        public Builder amountPredicted(int amountPredicted) {
            this.amountPredicted = amountPredicted;
            return this;
        }

        public MonthSummaryDTO build() {
            return new MonthSummaryDTO(
                    this.date,
                    this.numberOfClients,
                    this.numberOfAbsentClients,
                    this.numberOfClientsWithNoLastPayment,
                    this.amountPaidThisMonth,
                    this.amountPredicted
            );
        }
    }
}
