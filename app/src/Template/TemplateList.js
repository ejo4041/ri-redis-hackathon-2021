import { useEffect, useState } from 'react'
import './TemplateList.scss'
import templateService from '../services/template.service'
import TemplateListItem from './TemplateListItem'
import { Button, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField } from '@material-ui/core';


export default function TemplateList(props) {
    const [templateList, setTemplateList] = useState([]);
    const [selectedTemplate, setSelectedTemplate] = useState(null)
    const [creatingTemplate, setCreatingTemplate] = useState(false)
    const [newTemplateName, setNewTemplateName] = useState(false)
    
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
    const handleCreateTemplate = (e) => {
        setNewTemplateName(e.target.value);
    }
    const createTemplate = () => {
        templateService.addTemplate({"templateName": newTemplateName}).then(res => {
            debugger;
            if (res && res.template) {
                this.templateList.push(res.template);
                this.selectedTemplate = res.template;
            }
        });
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
                    
                    <TableRow>
                        {!creatingTemplate && <TableCell className="template-list-item" onClick={() => setCreatingTemplate(true)}>
                            + New Template
                        </TableCell>}
                        {creatingTemplate && <TableCell>
                            <TextField placeholder="New Template" onChange={handleCreateTemplate}></TextField>
                            <Button onClick={createTemplate}>Create</Button>
                        </TableCell>}
                    </TableRow>
                </TableBody>
            </Table>
        </TableContainer>
    )
}