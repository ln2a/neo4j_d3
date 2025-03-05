<template>
<div class="detail_panel">
    <h3>信息面板</h3>
    <!-- <p>{{ nodeMsg.id }}</p> -->
    <el-card style="width: 300px" class="elCard">
        <template #header>
            <div class="card-header">
                <span>节点信息</span>
            </div>
        </template>
        <div class="text item" v-show="cardFlag">
            <table style="width: 280px;">
                <tbody>
                    <tr v-for="(v,k) in nodeMsg">
                        <td class="detail-info">{{k}}</td>
                        <td class="detail-info">{{v}}</td>
                    </tr>
                </tbody>
            </table>

            <el-form v-show="selectCardFlag" label-width="auto" style="max-width: 600px">
                <el-form-item>
                    <el-select v-model="currentType" placeholder="请指定查询关系" class="submit-selector">
                        <el-option v-for="(k,i) of relationshipTypes" :label="k" :value="i" />
                    </el-select>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="onSubmit" class="submit-btn">查询关系</el-button>
                </el-form-item>
            </el-form>

        </div>
    </el-card>
</div>
</template>

<script>
import {
    ref,
    watch,
} from 'vue';
// import { ElMessage } from "element-plus";
export default {
    props: {
        msg: Object,
    },
    emits: ["inquire"],
    setup(props, {
        emit
    }) {
        const cardFlag = ref(false)
        const selectCardFlag = ref(false)
        // cardFlag.value = false;

        // 数据在props中，用ref构建为响应式，即可以修改值
        const nodeMsg = ref(props.msg);
        // const nodeInfo=ref({id:""})
        // console.log(nodeMsg.id);
        const currentType = ref("");
        const relationshipTypes = ["ACTED_IN", "DIRECTED", "PRODUCED", "REVIEWED", "WROTE"];
        // 定义触发的事件及其数据类型
        // const emit = defineEmits(['inquire']);

        // 监听数据变化,只有变化才会执行回调函数
        watch(() =>
            props.msg, (newValue) => {
                nodeMsg.value = newValue;
                console.log(nodeMsg.value);               
                if (!cardFlag.value && nodeMsg.value.id != "")
                    cardFlag.value = true;
                if (nodeMsg.value?.name === undefined)
                    selectCardFlag.value = false;
                else {
                    selectCardFlag.value = true;
                }

            }, {
                immediate: true
            }
        )

        // 指定查询关系事件
        function onSubmit() {
            // console.log(relationshipUrl[currentType.value]);

            if (currentType.value === "") {
                ElMessage({
                    message: '查询关系不可以为空',
                    type: 'warning',
                    plain: true,
                })
                // console.log("error");

            } else {
                emit('inquire', nodeMsg.value.name, currentType.value);
            }
        }

        return {
            nodeMsg,
            cardFlag,
            selectCardFlag,
            onSubmit,
            currentType,
            relationshipTypes
        }
    }
}
</script>

<style>
.detail_panel {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 300px;
    height: max-content;
    position: absolute;
    top: 50px;
    right: 3%;
    background-color: pink;
    text-align: center;
    /* border: 1px solid #2c3e50; */
    /* border-radius: 8px; */
}

.detail-info {
    width: 50%;
    height: max-content;
    font-size: 14px;
    text-align: left;
    font-weight: bold;
}

.submit-selector {
    margin-top: 20px;
}

.submit-btn {
    width: max-content;
    margin: 0 auto;
}
</style>
