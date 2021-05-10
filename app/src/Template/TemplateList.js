import { useEffect } from 'react'
import './TemplateList.scss'
import templateService from '../services/template.service'


export default function TemplateList() {

    useEffect(() => {
        templateService.getTemplates().then(res => {
            debugger;
        })
        // return () => {
        //     cleanup
        // }
    })
    return (
        <div className="template-list">
            <h1>Template List</h1>
        </div>
    )
}