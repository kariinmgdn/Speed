<script setup>
import DataTable from "primevue/datatable";
import Column from 'primevue/column';
import Button from "primevue/button";
import {computed, onMounted, ref, watch} from "vue";
import Accordion from 'primevue/accordion';
import AccordionTab from 'primevue/accordiontab';
import Calendar from 'primevue/calendar';
import InputNumber from 'primevue/inputnumber';
import {formatDate} from "./DateFormatter";
import {useSpeedStore} from "@/stores/SpeedStore";

const speedStore = useSpeedStore();

const data = ref([]);
const loading = ref(false);
const isMax = ref(false);
const dateFrom = ref();
const dateTo = ref();
const speed = ref();

computed(() => speedStore.speedData);
computed(() => speed);
computed(() => dateFrom);
computed(() => dateTo);

const loadData = async () => {
    const speedValue = speed.value ? speed.value : "";
    const from = dateFrom.value ? formatDate(dateFrom.value) : "";
    const to = dateTo.value ? formatDate(dateTo.value) : "";
    await speedStore.getSpeedData(from, to, speedValue);
    const allData = speedStore.speedData;
    isMax.value = allData.length !== 20;
    data.value = allData;
    loading.value = false;
}

watch(speedStore, (value, oldValue) => {
    data.value = value.speedData;
    if (value.pageIndex !== oldValue.pageIndex) {
        loading.value = true;
        loadData();
    }
})

const onRightClick = () => {
    speedStore.pageIndex++;
    loading.value = true;
    loadData();
}

const onFilter = () => {
    speedStore.pageIndex = 0;
    loadData();
}

const resetFilters = () => {
    dateFrom.value = null;
    dateTo.value = null;
    speed.value = null;
}

const onRemoveFilters = () => {
    speedStore.pageIndex = 0;
    resetFilters();
    loadData();
}

const onLeftClick = () => {
    speedStore.pageIndex--;
    loading.value = true;
    loadData();
}

onMounted(() => {
    loadData();
})
</script>

<template>
    <Accordion>
        <AccordionTab header="Filtrēt datus">
            <div class="filter-container">
                <div class="label-text">Laiks no</div>
                <Calendar v-model="dateFrom" showIcon showTime hourFormat="24"/>

                <div class="label-text">Laiks lídz</div>
                <Calendar v-model="dateTo" showIcon showTime hourFormat="24"/>

                <div class="label-text">Ātrums (km/h)</div>
                <InputNumber v-model="speed"/>
            </div>
            <div class="button-container">
                <Button label="Notīrīt filtrus" class="button" style="margin: 0.5rem" @click="onRemoveFilters"/>
                <Button label="Filtrēt" class="button" style="margin: 0.5rem 0.2rem" @click="onFilter"/>
            </div>
        </AccordionTab>
    </Accordion>
    <DataTable stripedRows :loading="loading" scrollable scrollHeight="400px" :value="data"
               tableStyle="min-width: 50rem">
        <Column field="time" header="Laiks" style="width: 50%"></Column>
        <Column field="speed" header="Ātrums km/h" style="width: 25%"></Column>
        <Column field="registrationPlate" header="Reģ. nr." style="width: 25%"></Column>
        <template #empty>
            Dati nav atrasti
        </template>
        <template #footer>
            <div style="width: 100%; display: flex; justify-content: center">
                <Button :disabled="speedStore.pageIndex <= 0" @click="onLeftClick" style="margin: 0 5px"
                        icon="pi pi-chevron-left"/>
                <Button :disabled="isMax" @click="onRightClick" style="margin: 0 5px" icon="pi pi-chevron-right"/>
            </div>
        </template>
    </DataTable>
</template>

<style scoped>

.label-text {
    text-align: right;
    align-self: center;
}

.filter-container {
    width: 100%;
    display: grid;
    justify-content: center;
    grid-template-columns: max-content max-content;
    grid-gap: 5px;
}

.button-container {
    width: 100%;
    display: flex;
    justify-content: end;
}
</style>
