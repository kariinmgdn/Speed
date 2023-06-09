<script setup>
import DataTable from "primevue/datatable";
import Column from 'primevue/column';
import Button from "primevue/button";
import {computed, onMounted, ref, watch} from "vue";
import Accordion from 'primevue/accordion';
import AccordionTab from 'primevue/accordiontab';
import Calendar from 'primevue/calendar';
import InputNumber from 'primevue/inputnumber';
import {useSpeedStore} from "@/stores/SpeedStore";
import {useToast} from "primevue/usetoast";
import Toast from 'primevue/toast';

const toast = useToast();
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
    speedStore.speed = speed.value;
    speedStore.from = dateFrom.value;
    speedStore.to = dateTo.value;

    await speedStore.getSpeedData();
    const allData = speedStore.speedData;
    if (speedStore.pageIndex !== 0 && allData.length === 0) {
        speedStore.pageIndex--;
        await loadData();
        isMax.value = true;
        show();
    } else {
        data.value = allData;
    }
    isMax.value = allData.length !== 20;
    loading.value = false;
}

const show = () => {
    toast.add({severity: 'info', summary: 'Info', detail: 'Tika ielādēti visi dati', life: 3000});

};

watch(speedStore, (value, oldValue) => {
    data.value = value.speedData;
    if (value.pageIndex !== oldValue.pageIndex) {
        loading.value = true;
        loadData();
    }
    dateFrom.value = value.from;
    dateTo.value = value.to;
    speed.value = value.speed;
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
    <Toast/>
    <Accordion>
        <AccordionTab header="Filtrēt datus">
            <div class="filter-container">
                <div class="label-text">Laiks no</div>
                <Calendar v-model="dateFrom" showIcon showTime hourFormat="24"/>

                <div class="label-text">Laiks līdz</div>
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
    <DataTable stripedRows :loading="loading" scrollable scrollHeight="350px" :value="data"
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
