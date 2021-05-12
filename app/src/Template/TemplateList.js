import { useEffect, useState } from 'react'
import './TemplateList.scss'
import templateService from '../services/template.service'
import TemplateListItem from './TemplateListItem'
import { Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@material-ui/core';


export default function TemplateList(props) {
    const [templateList, setTemplateList] = useState([]);
    const [selectedTemplate, setSelectedTemplate] = useState(null)

    useEffect(() => {
        templateService.getTemplates().then(res => {
            if(res && res.data.templateList) {
                setTemplateList(res.data.templateList)
            }
        })
        // return () => {
        //     cleanup
        // }
    }, [setTemplateList])

    const templateSelected = (tmpl) => {
        setSelectedTemplate(tmpl)
        props.templateSelected(tmpl)
    }
    return (
        <TableContainer component={Paper} className="template-list">
            <h2>My Templates</h2>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>
                            Template Name
                        </TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {templateList.map((tmpl, i) => 
                    <TableRow key={i}>
                        <TableCell 
                            className={"template-list-item " + (selectedTemplate && selectedTemplate.settingsId === tmpl.settingsId ? 'active' : '')}
                            onClick={() => templateSelected(tmpl)}>
                            <TemplateListItem template={tmpl} />
                        </TableCell>
                    </TableRow>
                    )}
                </TableBody>
            </Table>
        </TableContainer>
    )
}