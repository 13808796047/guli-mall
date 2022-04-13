<!--  -->
<template>
  <div>
    <el-tree
      :data="menus"
      show-checkbox
      node-key="catId"
      :props="defaultProps"
      :expand-on-click-node="false"
      :default-expanded-keys="expandedKey"
    >
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button
            v-if="node.level <= 2"
            type="text"
            size="mini"
            @click="() => append(data)"
          >
            添加
          </el-button>
          <el-button type="text" size="mini" @click="() => edit(data)">
            编辑
          </el-button>
          <el-button
            v-if="node.childNodes.length == 0"
            type="text"
            size="mini"
            @click="() => remove(node, data)"
          >
            删除
          </el-button>
        </span>
      </span>
    </el-tree>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogVisible"
      width="30%"
    >
      <el-form label-width="80px" :model="category">
        <el-form-item label="分类名称">
          <el-input v-model="category.name"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="category.icon"></el-input>
        </el-form-item>
        <el-form-item label="计量单位">
          <el-input v-model="category.product_unit"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
// 例如：import 《组件名称》 from '《组件路径》';

export default {
  // import引入的组件需要注入到对象中才能使用
  components: {
    name: '',
    parentCid: 0,
    catLevel: 0,
    sort: 0,
    showStatus: 1,
    catId: null,
    icon: null,
    productUnit: null
  },
  data () {
    // 这里存放数据
    return {
      title: '',
      category: {},
      dialogVisible: false,
      expandedKey: [],
      menus: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {},
  // 方法集合
  methods: {
    // 添加三级分类
    addCategory () {
      this.$http({
        url: this.$http.adornUrl('/product/category/save'),
        method: 'post',
        data: this.$http.adornData(this.category, false)
      }).then(({ data }) => {
        this.$message({
          message: '菜单保存成功',
          type: 'success'
        })
        // 关闭对话框
        this.dialogVisible = false
        // 刷新出新的菜单
        this.getDataList()
        // 设置需要默认展开的菜单
        this.expandedKey = [this.category.parentCid]
      })
    },
    edit (data) {
      this.dialogVisible = true
      // 发送请求获取当前节点最新的数据
      this.$http({
        url: this.$http.adornUrl(`/product/category/info/${data.catId}`),
        method: 'get',
        params: this.$http.adornParams({})
      }).then(({ data }) => {
        this.category = data.category
        this.title = '编辑分类'
      })
    },
    // 获取数据列表
    getDataList () {
      this.dataListLoading = true
      this.$http({
        url: this.$http.adornUrl('/product/category/list/tree'),
        method: 'get'
      }).then(({ data }) => {
        this.menus = data.categories
        this.dataListLoading = false
      })
    },

    append (data) {
      this.title = '新增分类'
      this.dialogVisible = true
      this.category = {}
      this.category.parentCid = data.catId
      this.category.catLevel = data.catLevel * 1 + 1
    },
    submitForm () {
      this.category.catId ? this.editCategory() : this.addCategory()
    },
    editCategory () {
      let { catId, name, icon, productUnit } = this.category
      let data = { catId, name, icon, productUnit }
      this.$http({
        url: this.$http.adornUrl('/product/category/update'),
        method: 'post',
        data: this.$http.adornData(data, false)
      }).then(({ data }) => {
        this.$message({
          message: '菜单修改成功',
          type: 'success'
        })
        // 关闭对话框
        this.dialogVisible = false
        // 刷新出新的菜单
        this.getDataList()
        // 设置需要默认展开的菜单
        this.expandedKey = [this.category.parentCid]
      })
    },
    remove (node, data) {
      var ids = [data.catId]
      this.$confirm(`是否删除【${data.name}】菜单?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.$http({
            url: this.$http.adornUrl('/product/category/delete'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({ data }) => {
            this.$message({
              message: '菜单删除成功',
              type: 'success'
            })
            // 刷新出新的菜单
            this.getDataList()
            // 设置需要默认展开的菜单
            this.expandedKey = [node.parent.data.catId]
          })
        })
        .catch(() => {})
    }
  },
  // 生命周期 - 创建完成（可以访问当前this实例）
  created () {
    this.getDataList()
  },
  // 生命周期 - 挂载完成（可以访问DOM元素）
  mounted () {},
  beforeCreate () {}, // 生命周期 - 创建之前
  beforeMount () {}, // 生命周期 - 挂载之前
  beforeUpdate () {}, // 生命周期 - 更新之前
  updated () {}, // 生命周期 - 更新之后
  beforeDestroy () {}, // 生命周期 - 销毁之前
  destroyed () {}, // 生命周期 - 销毁完成
  activated () {} // 如果页面有keep-alive缓存功能，这个函数会触发
}
</script>
<style scoped>
/* // @import url(); 引入公共css类 */
</style>
