package com.saiyi.ui.Entity;

import java.util.List;

/**
 * Created by 陈姣姣 on 2018/7/25.
 */

public class DayXiangQingBean {


    /**
     * success : true
     * code : 1000
     * message : 查询成功
     * data : [{"date":"2018-07-13","weeks":"星期五","equdataDetails":[{"frequency":"1","time":"13:34","concentration":"01","ultrafiltration":"0020"},{"frequency":"2","time":"14:49","concentration":"01","ultrafiltration":"0060"},{"frequency":"3","time":"15:14","concentration":"1","ultrafiltration":"50"}]}]
     */

    private boolean success;
    private int code;
    private String message;
    /**
     * date : 2018-07-13
     * weeks : 星期五
     * equdataDetails : [{"frequency":"1","time":"13:34","concentration":"01","ultrafiltration":"0020"},{"frequency":"2","time":"14:49","concentration":"01","ultrafiltration":"0060"},{"frequency":"3","time":"15:14","concentration":"1","ultrafiltration":"50"}]
     */

    private List<DataBean> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String date;
        private String weeks;
        /**
         * frequency : 1
         * time : 13:34
         * concentration : 01
         * ultrafiltration : 0020
         */

        private List<EqudataDetailsBean> equdataDetails;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getWeeks() {
            return weeks;
        }

        public void setWeeks(String weeks) {
            this.weeks = weeks;
        }

        public List<EqudataDetailsBean> getEqudataDetails() {
            return equdataDetails;
        }

        public void setEqudataDetails(List<EqudataDetailsBean> equdataDetails) {
            this.equdataDetails = equdataDetails;
        }

        public static class EqudataDetailsBean {
            private String frequency;
            private String time;
            private String concentration;
            private String ultrafiltration;

            public String getFrequency() {
                return frequency;
            }

            public void setFrequency(String frequency) {
                this.frequency = frequency;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getConcentration() {
                return concentration;
            }

            public void setConcentration(String concentration) {
                this.concentration = concentration;
            }

            public String getUltrafiltration() {
                return ultrafiltration;
            }

            public void setUltrafiltration(String ultrafiltration) {
                this.ultrafiltration = ultrafiltration;
            }
        }
    }
}
