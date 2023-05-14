<script setup>
import {RouterLink} from 'vue-router'
import TabPanel from "primevue/tabpanel";
import TabView from "primevue/tabview";
import AverageSpeedView from "@/views/AverageSpeedView.vue";
import SpeedTableView from "@/views/SpeedTableView.vue";
import {onMounted, ref} from "vue";
import router from "@/router";

const path = location.pathname;
const active = ref();

onMounted(() => {
    if (path === "/" || path === "/average") {
        active.value = path === "/" ? 0 : 1;
    } else {
        router.push("/");
    }
})
</script>

<template>
    <div style="display: block">
        <h1 style="display: flex;justify-content: center; margin-bottom: 2rem">Ātruma rādītāji</h1>
        <div class="p-card">
            <TabView :active-index="active" class="panel">
                <TabPanel>
                    <template #header>
                        <RouterLink style="padding: 1rem; color: #181818" to="/"> Tabula</RouterLink>
                    </template>
                    <div>
                        <SpeedTableView/>
                    </div>
                </TabPanel>
                <TabPanel>
                    <template #header>
                        <RouterLink style="padding: 1rem; color: #181818" to="/average"> Vidējais atrums</RouterLink>
                    </template>
                    <AverageSpeedView/>
                </TabPanel>
            </TabView>
        </div>
    </div>
</template>

<style scoped lang="scss">

.panel:deep {
  .p-tabview-nav-container {
    .p-tabview-nav-link {
      padding: 0;
    }
  }
}
</style>
