package shop.mtcoding.projoctbodykey.activity;

import lombok.Data;
import shop.mtcoding.projoctbodykey.bodydata.BodyData;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ActivityResponse {

    @Data
    public static class activitiesDateDTO {
        private Timestamp createdAt;
        private Integer walking;
        private Integer drinkWater;
        private Integer kcal;
        private Double weight;

        public activitiesDateDTO(Activity activity, BodyData bodyData) {
            if (activity != null) {
                this.walking = activity.getWalking();
                this.drinkWater = activity.getDrinkWater();
            } else {
                this.walking = 0;
                this.drinkWater = 0;
            }
            this.kcal = 0;
            if (bodyData != null) {
                Timestamp timestamp = bodyData.getCreatedAt();

                // UTC 기준의 Timestamp를 LocalDateTime으로 변환합니다.
                LocalDateTime utcDateTime = timestamp.toLocalDateTime();

                // LocalDateTime을 한국 표준시(KST)로 변환합니다.
                LocalDateTime kstDateTime = utcDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDateTime();

                // 한국 표준시(KST)로 변환한 LocalDateTime을 Timestamp로 다시 변환합니다.
                this.createdAt = Timestamp.valueOf(kstDateTime);
                this.weight = bodyData.getWeight();
            } else {
                LocalDateTime now = LocalDateTime.now();
                this.createdAt = Timestamp.valueOf(now);
                this.weight = 0.0d;
            }
        }
    }

    @Data
    public static class mainDTO {
        private String username;
        private double goalfat;
        private double fat;
        private double goalmuscle;
        private double muscle;
        private Integer walking;
        private Integer drinkWater;
        private Integer mealCount;
        private List<BodydataDTO> bodys = new ArrayList<>();

        public mainDTO(List<BodyData> bodyList) {
            this.username = username;
            this.goalfat = goalfat;
            this.fat = fat;
            this.goalmuscle = goalmuscle;
            this.muscle = muscle;
            this.walking = walking;
            this.drinkWater = drinkWater;
            this.mealCount = mealCount;
            this.bodys = bodyList.stream().map(BodydataDTO::new).toList();
        }

        public static class BodydataDTO {
            private double weight;
            private double muscle;
            private double fat;
            private String createdAt;

            public BodydataDTO(BodyData body) {
                this.weight = body.getWeight();
                this.muscle = body.getMuscle();
                this.fat = body.getFat();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String Date = body.getCreatedAt().toLocalDateTime().format(formatter);
                this.createdAt = Date;
            }
        }
    }

    @Data
    public static class WalkingDetail {
        private Integer dayAcitivityId;
        private Integer dayWalking;
        private Long totalMonthWalking;
        private double avgMonthWalking;
        private double rateAvgWalking;
        private Integer maxWalking;
        private Timestamp maxWalkingDay;
        private List<WeekWalkingDTO> weekWalkings;

        @Data
        public static class WeekWalkingDTO{
            private Timestamp date;
            private Integer walking;

            public WeekWalkingDTO(ActivityRequest.WeekWalking weekWalking) {
                this.date = weekWalking.getDate();
                this.walking = weekWalking.getWalking();
            }
        }

        public WalkingDetail(Activity activity, ActivityRequest.WalkingToatalAndAVG  WalkingToatalAndAVG,
                             double rateAvgWalking, ActivityRequest.MaxWalkingDay maxWalking, List<ActivityRequest.WeekWalking> weekWalkings) {
            this.dayAcitivityId = activity.getId();
            this.dayWalking = activity.getWalking();
            this.totalMonthWalking = WalkingToatalAndAVG.getTotalMonthWalking();
            this.avgMonthWalking = (int) (WalkingToatalAndAVG.getAvgMonthWalking()* 100) / 100.0;
            this.rateAvgWalking = (int) (rateAvgWalking * 100) / 100.0;
            this.maxWalking = maxWalking.getMaxWalking();
            this.maxWalkingDay = maxWalking.getMaxWalkingDay();
            this.weekWalkings = weekWalkings.stream().map(WeekWalkingDTO::new).toList();
        }
    }

    @Data
    public static class WaterDetail {
        private Integer dayAcitivityId;
        private Integer dayWater;
        private List<WeekWaterDTO> weekWater;

        @Data
        public static class WeekWaterDTO {
            private Timestamp date;
            private Integer water;

            public WeekWaterDTO(ActivityRequest.WeekWater weekWater) {
                this.date = weekWater.getDate();
                this.water = weekWater.getWater();
            }
        }

        public WaterDetail(Activity activity, List<ActivityRequest.WeekWater> weekWater) {
            this.dayAcitivityId = activity.getId();
            this.dayWater = activity.getDrinkWater();
            this.weekWater = weekWater.stream().map(WeekWaterDTO::new).toList();
        }
    }

    @Data
    public static class UpdateDTO {
        private Integer userId;
        private Integer walking;
        private Integer water;

        public UpdateDTO(Integer userId, Activity activity) {
            this.userId = userId;
            this.walking = activity.getWalking();
            this.water = activity.getDrinkWater();
        }
    }

    // 아래는 걷기, 물 따로 업데이트 하는거
    @Data
    public static class WalkingUpdateDTO {
        private Integer userId;
        private Integer walking;

        public WalkingUpdateDTO(Integer userId, Integer walking) {
            this.userId = userId;
            this.walking = walking;
        }
    }

    @Data
    public static class WaterUpdateDTO {
        private Integer userId;
        private Integer water;

        public WaterUpdateDTO(Integer userId, Integer water) {
            this.userId = userId;
            this.water = water;
        }
    }
}
