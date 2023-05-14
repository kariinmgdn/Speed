import {defineStore} from "pinia";
import axios from "axios";
import {ref} from "vue";

export const useSpeedStore = defineStore({
    id: "speedStore",
    state: () => ({
        speedData: ref([]),
        averageSpeedData: ref([]),
        pageIndex: ref(0),
    }),
    actions: {
        async getSpeedData(from, to, speed) {
            await axios.get("/api/speed", {
                params: {
                    from: from, to: to, speed: speed, page: this.pageIndex
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