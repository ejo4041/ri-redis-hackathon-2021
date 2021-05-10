import axios from 'axios'

let URL = 'http://localhost:8082/api/v1/admin/template'

class Template {
    addTemplate(data) {
        axios.post(`${URL}/create`, data)
    }
    addTemplateSetting(data, id) {
        axios.post(`${URL}/create/${id}/templateSetting`, data)
    }
    deleteTemplate(id) {
        axios.delete(`${URL}/delete/${id}`)
    }
    deleteTemplateSetting(id, key) {
        axios.post(`${URL}/delete/${id}/templateSetting/${key}`)
    }
    getTemplates() {
        return axios.get(`${URL}/get`, {
            headers: {
            'Content-Type': 'application/json'
        }})
    }
    updateTemplate(data) {
        axios.put(`${URL}/update`, data)
    }
    updateTemplateName(id, tplName) {
        axios.put(`${URL}/create/${id}/templateName/${tplName}`)
    }
    updateTemplateSetting(data, id) {
        axios.put(`${URL}/create/${id}/templateSetting`, data)
    }
}

export default new Template();