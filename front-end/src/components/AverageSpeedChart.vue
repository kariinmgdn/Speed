<script setup>
import Chart from 'primevue/chart';
import {computed, onMounted, ref, watch} from "vue";
import {formatDate, formatNumbers} from "@/components/DateFormatter";
import Button from 'primevue/button';
import Calendar from 'primevue/calendar';
import {useSpeedStore} from "@/stores/SpeedStore";

const speedStore = useSpeedStore();

const chartData = ref({labels: [], datasets: {data: []}});
const chartOptions = ref({
    scales: {
        y: {
            beginAtZero: true
        }
    }
});
const date = ref(new Date(2020, 7, 1));

computed(() => chartData);
computed(() => speedStore);

watch(speedStore, (value) => {
    const data = value.averageSpeedData;
    Object.assign(chartData.value, {
        labels: data.map(averageSpeed => {
            const timePeriod = averageSpeed.hour < 23 ? `:00 - ${averageSpeed.hour + 1}:00` : ": 00 - 00:00";
            return formatNumbers(averageSpeed.hour) + timePeriod;
        }),
        datasets: [
            {
                label: "Vidējais ātrums km/h",
                data: data.map(averageSpeed => averageSpeed.speed),
                backgroundColor: ['rgba(255, 183, 206, 0.3)'],
                borderColor: ['rgb(255, 183, 206)'],
                borderWidth: 1
            }
        ]
    });
})

const loadData = async () => {
    await speedStore.getAverageSpeedData(formatDate(date.value));
}

onMounted(() => {
    loadData();
})
</script>

<template>
    <div style="width: 100%; display: flex; justify-content: end; margin: 10px 0">
        <Calendar v-model="date" showIcon @keypress.enter="loadData()"/>
        <Button style="margin:0 5px" class="btn" label="Meklēt" @click="loadData()"/>
    </div>
    <Chart style="width: 50rem" type="bar" :data="chartData" :options="chartOptions"/>
</template>

<style scoped>
</style>