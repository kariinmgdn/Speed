<script setup>
import FileUpload from 'primevue/fileupload';
import Dialog from 'primevue/dialog';
import {ref} from "vue";
import {useSpeedStore} from "@/stores/SpeedStore";
import {formatDate} from "@/components/DateFormatter";

const speedStore = useSpeedStore();

const isBusy = ref(false);

const onUpload = () => {
    speedStore.pageIndex = 0;
    speedStore.from = null;
    speedStore.to = null;
    speedStore.speed = null;
    speedStore.getSpeedData();
    speedStore.getAverageSpeedData(formatDate(new Date(2020, 7, 1)));
    isBusy.value = false;
};

const onSend = () => {
    isBusy.value = true;
}
</script>

<template>
    <div style="width: 100%; display: flex; justify-content: end; margin: 10px 0">
        <FileUpload @before-send="onSend()" mode="basic" url="./api/speed/uploadFile" name="file" accept="text/*"
                    :maxFileSize="10000000" @upload="onUpload()" :auto="true" chooseLabel="Pievienot datus"/>
    </div>
    <Dialog modal v-model:visible="isBusy" header="Notiek datu sūtīšana..." :closable="false">
        <div style=" width: 100%; display: flex; justify-content: center">
            <i class="pi pi-spin pi-spinner" style="font-size: 7rem"></i>
        </div>
    </Dialog>
</template>

<style scoped>

</style>