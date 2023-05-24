import {defineStore} from "pinia";
import axios from "axios";
import {ref} from "vue";
import {formatDate} from "@/components/DateFormatter";

export const useSpeedStore = defineStore({
    id: "speedStore",
    state: () => ({
        speedData: ref([]),
        averageSpeedData: ref([]),
        pageIndex: ref(0),
        from: ref(null),
        to: ref(null),
        speed: ref(null),
    }),
    actions: {
        async getSpeedData() {
            await axios.get("/api/speed", {
                params: {
                    from: formatDate(this.from),
                    to: formatDate(this.to),
                    speed: this.speed ? this.speed : "",
                    page: this.pageIndex
                }
            }).then((response) => {
                this.speedData = response.data;
            }).catch((error) => {
                this.speedData = [];
                console.log(error);
            });
        },
        async getAverageSpeedData(date) {
            await axios.get("/api/speed/average",
                {
                    params: {
                        date: date
                    }
                }).then((response) => {
                this.averageSpeedData = response.data;
            }).catch((error) => {
                console.log(error);
                this.averageSpeedData = [];
            });
        }
    }
})