import axios from 'axios'

let URL = 'http://localhost:8081/api/v1/admin/template'

class Template {
    axiosInstance = axios.create({
        baseURL: URL,
        headers: { 'Content-type': 'application/json' },
      });

    addTemplate(data) {
        return this.axiosInstance.post(`/create`, data)
    }
    addTemplateSetting(data, id) {
        return this.axiosInstance.post(`/create/${id}/templateSetting`, data)
    }
    deleteTemplate(id) {
        return this.axiosInstance.delete(`/delete/${id}`)
    }
    deleteTemplateSetting(id, key) {
        return this.axiosInstance.delete(`/delete/${id}/templateSetting/${key}`)
    }
    getTemplate(id) {
        return this.axiosInstance.get(`/get/${id}`)
    }
    getTemplates() {
        return this.axiosInstance.get(`/get`)
    }
    updateTemplate(data) {
        return this.axiosInstance.put(`/update`, data)
    }
    updateTemplateName(id, tplName) {
        return this.axiosInstance.put(`/create/${id}/templateName/${tplName}`)
    }
    updateTemplateSetting(data, id) {
        return this.axiosInstance.put(`/create/${id}/templateSetting`, data)
    }
}

export default new Template();