<template>
<h3>力导向图</h3>
<div class="canvs">
    <div class="container"></div>
    <DetailPanel :msg="msg" @inquire="inquireHandle" />
</div>
</template>

<script>
import * as d3 from 'd3';
import testGraph from '../assets/data.json'
import {
    onMounted,
    ref
} from 'vue'
import axios from 'axios';
import DetailPanel from './DetailPanel.vue';
export default {
    components: {
        DetailPanel,
    },

    setup() {
        const GraphData = ref(testGraph)
        GraphData.value.links = [];
        GraphData.value.nodes = [];
        const simulation = ref(null);
        const node = ref(null);
        const link = ref(null);
        const nodeName = ref(null);
        const linkName = ref(null);
        const msg = ref({
            id: ''
        });

        // GraphData.value.links = [{
        //     "source": "Paul Blythe",
        //     "target": "Angela Scope",
        //     "value": 10
        // }];

        // main函数
        onMounted(() => {
            // test()
            // axios.get("/api" + "/person/" + "all")
            axios.get("/api" + "/person/" + "Laurence Fishburne")
            // axios.get("/api" + "/person/" + "Rob Reiner")
                .then((responce) => {
                    console.log(responce.data);
                    // 查询到的后端数据——可视化
                    GraphData.value.nodes = Array.isArray(responce.data) ? responce.data : [responce.data];
                    // GraphData.value.nodes.push(responce.data)
                    // GraphData.value.nodes=responce.data

                    initGraph(GraphData.value)

                })
                .catch((error) => {
                    console.log(error);
                })
        })

        // 结点点击事件-增加对应关系节点
        function selectNode(e, d) {
            // console.log(d);
            // console.log(typeof(d));
            let data = {}
            for (var i in d.obj)
                if (!Array.isArray(d.obj[i]))
                    data[i] = d.obj[i]
            msg.value = data;
            console.log(msg.value);
        }

        const relationshipUrl = ["actedby", "directed", "produced", "reviewed", "wrote"];
        const relationshipTypes = ["ACTED_IN", "DIRECTED", "PRODUCED", "REVIEWED", "WROTE"];
        //子组件触发的查询函数
        const inquireHandle = (id, relationshipID) => {
            var relationship = relationshipUrl[relationshipID]
            console.log(relationship);
            axios.get("/api" + "/person/query/" + relationship + "/" + id)
                .then((responce) => {
                    if (responce.status == 200) {
                        // console.log(responce);
                        var flag = true
                        for (var k = 0; k < GraphData.value.links.length; k++) {
                            if (GraphData.value.links[k].source === id) {
                                if (GraphData.value.links[k].relationship !== relationshipTypes[relationshipID]) {
                                    for (var h = 0; h < GraphData.value.nodes.length; h++) {
                                        // 删除已有节点
                                        if (GraphData.value.nodes[h].id === GraphData.value.links[k].target) {
                                            GraphData.value.nodes.splice(h, 1);
                                            break;
                                        }
                                    }
                                    // 删除已有边
                                    GraphData.value.links.splice(k, 1);
                                    k = -1;
                                } else {
                                    // 如果有相同关系的边，则不更新
                                    flag = false;
                                }
                            }
                        }
                        // for (var i = 0; i < responce.data.length; i++) {
                        //     // 判断新的电影节点是否已经加入node中
                        //     for (var j = 0; j < GraphData.value.nodes.length; j++)
                        //         if (responce.data[i].id == GraphData.value.nodes[j].id) {
                        //             flag = false;
                        //             break;
                        //         }
                        //     if (flag) {
                        //         // 加入新的电影节点，并重新绘制
                        //         GraphData.value.nodes.push(responce.data[i])
                        //         GraphData.value.links.push({
                        //             "source": id,
                        //             "target": responce.data[i].id,
                        //             "value": 10,
                        //             "relationship": relationshipTypes[relationshipID],
                        //         })

                        //     }
                        // }
                        // if (flag){}
                        //     updateGraph(GraphData.value)
                        if (flag) {
                            for (var i = 0; i < responce.data.length; i++) {
                                GraphData.value.nodes.push(responce.data[i])
                                GraphData.value.links.push({
                                    "source": id,
                                    "target": responce.data[i].id,
                                    "value": 10,
                                    "relationship": relationshipTypes[relationshipID],
                                })
                            }
                            updateGraph(GraphData.value)
                        }
                        console.log(GraphData.value);
                    }
                })
                .catch((error) => {
                    console.log(error);
                })
        };

        // 力导向图绘制
        function initGraph(data) {
            // Specify the dimensions of the chart.
            const width = 928;
            const height = 600;

            // Specify the color scale.
            const color = d3.scaleOrdinal(d3.schemeCategory10);

            // The force simulation mutates links and nodes, so create a copy
            // so that re-evaluating this cell produces the same result.
            const links = data.links.map(d => ({
                ...d
            }));
            const nodes = data.nodes.map(d => ({
                ...d
            }));

            // Create a simulation with several forces.
            simulation.value = d3.forceSimulation(nodes)
                .force("link", d3.forceLink(links).id(d => d.id).distance(200))
                .force("collide", d3.forceCollide().radius(() => 50))
                .force("charge", d3.forceManyBody().strength(-30))
                .force("center", d3.forceCenter(width / 2, height / 2))
                .on("tick", ticked);

            // Create the SVG container.
            // const svg = d3.create("svg")
            //     .attr("width", width)
            //     .attr("height", height)
            //     .attr("viewBox", [0, 0, width, height])
            //     .attr("style", "max-width: 100%; height: auto;");

            const svg = d3.select(".container").append("svg")
                .attr("width", width)
                .attr("height", height)
                .attr("viewBox", [0, 0, width, height])
                .attr("style", "max-width: 100%; height: auto;");

            // 为svg添加鼠标缩放功能
            svg.call(d3.zoom().on("zoom", function (event) {
                g.attr("transform", event.transform)
            }))

            // 添加箭头
            addMarkers(svg)

            const g = svg.append("g")
                .attr("class", "content")

            // Add a line for each link, and a circle for each node.
            link.value = g.append("g")
                .attr("stroke", "#999")
                .attr("stroke-opacity", 0.6)
                .selectAll()
                .data(links, d => d.source + "_" + d.relationship + "_" + d.target)
                .join("path")
                .attr("class", "link")
                .attr("stroke-width", d => Math.sqrt(d.value))
                .attr("id", d => d.source + "_" + d.relationship + "_" + d.target)

            node.value = g.append("g")
                // .attr("stroke", "#fff")
                // .attr("stroke-width", 1.5)
                .selectAll()
                .data(nodes,d=>d.id)
                .join("circle")
                .attr("r", 30)
                .attr("fill", d => color(d.group))
                .attr("class", "node")
                .on("click", selectNode)

            node.value.append("title")
                .text(d => d.id);

            // 节点标签
            nodeName.value = g.append("g")
                .selectAll("text")
                .data(nodes,d=>d.id)
                .join("text")
                .text(d => d.id)
                .attr("text-anchor", "middle")
                .attr("dy", 50)
                .style("font-size", "18px")
                .attr("class", "nodeName")

            // 边标签
            linkName.value = g.append("g")
                .selectAll("text")
                .data(links, d => d.source + "_" + d.relationship + "_" + d.target)
                .join("text")
                .attr("text-anchor", "middle")
                // .attr("x", 150)
                .append("textPath")
                .attr('xlink:href', d => "#" + d.source + "_" + d.relationship + "_" + d.target)
                .attr("startOffset", "50%")
                .style("fill", "White")
                .style("font-size", "10px")
                .style("font-weight", "bold")
                .text(d => d.relationship)
            // xlink:href="#A_likes_B" 使得<textpath>跟随 id="A_likes_B" 的 <path>

            // Add a drag behavior.
            node.value.call(d3.drag()
                .on("start", dragstarted)
                .on("drag", dragged)
                .on("end", dragended));
            // Set the position attributes of links and nodes each time the simulation ticks.
            function ticked() {
                link.value.each(function (d) {
                    const linkElement = d3.select(this);
                    if (d.source.x < d.target.x) {
                        linkElement.attr("d", d => `M ${d.source.x},${d.source.y} L ${d.target.x},${d.target.y}`) // M 是起点，L 是终点
                            .attr("marker-start", null)
                            .attr("marker-end", "url(#positiveMarker)")
                    } else {
                        linkElement.attr("d", d => `M ${d.target.x},${d.target.y} L ${d.source.x},${d.source.y}`)
                            .attr("marker-start", "url(#negativeMarker)")
                            .attr("marker-end", null)
                    }
                })
                // .attr("x1", d => d.source.x)
                // .attr("y1", d => d.source.y)
                // .attr("x2", d => d.target.x)
                // .attr("y2", d => d.target.y);

                node.value
                    .attr("cx", d => d.x)
                    .attr("cy", d => d.y);
                nodeName.value
                    .attr("x", d => d.x)
                    .attr("y", d => d.y);
            }
        }

        // 结点更新绘制
        function updateGraph(data) {
            // console.log(node.value);

            const links = data.links.map(d => ({
                ...d
            }));
            const nodes = data.nodes.map(d => ({
                ...d
            }));
            const color = d3.scaleOrdinal(d3.schemeCategory10);

            // 新加入的节点绑定数据
            const spliceNode = node.value.data(nodes, d => d.id);
            spliceNode.exit().remove();
            const enterNode = spliceNode
                .enter()
                .append("circle")
                .attr("r", 30)
                // .attr("fill", d => color(d.group))
                .attr("class", "node")
                .on("click", selectNode)
            // 合并原来的数据
            node.value = enterNode.merge(spliceNode)
            // **确保所有节点更新颜色**
            node.value.attr("fill", d => color(d.group));
            node.value.append("title")
                .text(d => d.id);
            node.value.call(d3.drag()
                .on("start", dragstarted)
                .on("drag", dragged)
                .on("end", dragended));

            //新加入的边
            const spliceLink = link.value.data(links, d => d.source + "_" + d.relationship + "_" + d.target);
            spliceLink.exit().remove();
            const enterLink = spliceLink
                .enter()
                .append("path")
                .attr("stroke", "red")
                .attr("stroke-opacity", 0.6)
                .attr("marker-end", "url(#positiveMarker)")
                .attr("class", "link")
                .attr("stroke-width", d => Math.sqrt(d.value))
                .attr("id", d => d.source + "_" + d.relationship + "_" + d.target)
            link.value = enterLink.merge(spliceLink)

            // 新加入的id——text
            const spliceNodeName = nodeName.value.data(nodes, d => d.id)
            spliceNodeName.exit().remove()
            const enterNodeName = spliceNodeName
                .enter()
                .append("text")
                .attr("text-anchor", "middle")
                .attr("dy", 50)
                .style("font-size", "18px")
                .attr("class", "nodeName")
            nodeName.value = enterNodeName.merge(spliceNodeName)
            nodeName.value.text(d => d.id)

            // 新加入的边——text
            const spliceLinkName = linkName.value.data(links, d => d.source + "_" + d.relationship + "_" + d.target)
            spliceLinkName.exit().remove()
            const enterLinkName = spliceLinkName
                .enter()
                .append("text")
                .attr("text-anchor", "middle")
                // .attr("x", 150)
                // .attr("y", 300)
                .append("textPath")
                .attr('xlink:href', d => "#" + d.source + "_" + d.relationship + "_" + d.target)
                .attr("startOffset", "50%")
                .style("fill", "White")
                .style("font-size", "10px")
                .style("font-weight", "bold")
            linkName.value = enterLinkName.merge(spliceLinkName)
            linkName.value.text(d => d.relationship)

            // 更新力的布局
            // 更新 nodes 和 links
            simulation.value.nodes(nodes);
            simulation.value.force("link").links(links);
            // 重新启动力导向图
            simulation.value.alpha(0.1).restart();

        }

        // 添加箭头函数
        function addMarkers(svgArea) {
            // 正向箭头
            const positiveMarker = svgArea.append("marker")
                .attr("id", "positiveMarker")
                .attr("orient", "auto")
                .attr("stroke-width", 2)
                .attr("markerUnits", "strokeWidth")
                .attr("markerUnits", "userSpaceOnUse")
                .attr("viewBox", "0 -5 10 10")
                .attr("refX", 35)
                .attr("refY", 0)
                .attr("markerWidth", 12)
                .attr("markerHeight", 12)
                .append("path")
                .attr("d", "M 0 -5 L 10 0 L 0 5")
                .attr('fill', 'red')
                .attr("stroke-opacity", 1.0);
            // 反向箭头
            const negativeMarker = svgArea.append("marker")
                .attr("id", "negativeMarker")
                .attr("orient", "auto")
                .attr("stroke-width", 2)
                .attr("markerUnits", "strokeWidth")
                .attr("markerUnits", "userSpaceOnUse")
                .attr("viewBox", "0 -5 10 10")
                .attr("refX", -25)
                .attr("refY", 0)
                .attr("markerWidth", 12)
                .attr("markerHeight", 12)
                .append("path")
                .attr("d", "M 10 -5 L 0 0 L 10 5")
                .attr('fill', 'red')
                .attr("stroke-opacity", 1.0);
        }

        // Reheat the simulation when drag starts, and fix the subject position.
        function dragstarted(event) {
            if (!event.active) simulation.value.alphaTarget(0.3).restart();
            event.subject.fx = event.subject.x;
            event.subject.fy = event.subject.y;
        }

        // Update the subject (dragged node) position during drag.
        function dragged(event) {
            event.subject.fx = event.x;
            event.subject.fy = event.y;
        }

        // Restore the target alpha so the simulation cools after dragging ends.
        // Unfix the subject position now that it’s no longer being dragged.
        function dragended(event) {
            if (!event.active) simulation.value.alphaTarget(0);
            event.subject.fx = null;
            event.subject.fy = null;
        }

        return {
            msg,
            inquireHandle
        }

    }
}
</script>

<style>
.canvs {
    position: relative;
}

.container {
    width: 800px;
    height: max-content;
    border: 1px solid #2c3e50;
    border-radius: 8px;
    margin-top: 40px;
    margin-left: auto;
    margin-right: auto;
    background: #154360 repeating-linear-gradient(30deg,
            hsla(0, 0%, 100%, .1), hsla(0, 0%, 100%, .1) 15px,
            transparent 0, transparent 30px);
    /* background: #154360 repeating-linear-gradient(30deg,
            hsla(0, 0%, 100%, .1), hsla(0, 0%, 100%, .1) 15px,
            transparent 0, transparent 30px); */
}

.node {
    stroke: #fff;
    stroke-width: 1;
    cursor: pointer;
}

.node:hover {
    stroke-width: 5;
}

.nodeName {
    fill: white;
}
</style>
