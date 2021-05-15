import { useEffect, useState } from 'react'
import templateService from '../services/template.service'
import TemplateListItem from './TemplateListItem'
import { Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, withStyles } from '@material-ui/core';
import TemplateCreate from './TemplateCreate';

export default function TemplateList(props) {
    const [templateList, setTemplateList] = useState([]);
    const [selectedTemplate, setSelectedTemplate] = useState(null)
    
    useEffect(() => {
        getTemplates();
    }, [setTemplateList])

    const getTemplates = () => {
        templateService.getTemplates().then(res => {
            if(res && res.data.templateList) {
                setTemplateList(res.data.templateList)
            }
        })
    }

    const selectTemplate = (tmpl) => {
        setSelectedTemplate(tmpl)
        props.templateSelected(tmpl)
    }
    
    const templateCreatedHandler = (newTpl) => {
        setTemplateList([...templateList, newTpl])
        selectTemplate(newTpl)
    }

    const deleteHandler = (id) => {
        templateService.deleteTemplate(id).then(res => {
            getTemplates();
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
                    <TableRow key={i} hover={true}>
                        <TableCell
                            style={{cursor: 'pointer'}}
                            padding="none" 
                            className={"template-list-item " + (selectedTemplate && selectedTemplate.settingsId === tmpl.settingsId ? 'active' : '')}
                            onClick={() => selectTemplate(tmpl)}>
                                    <TemplateListItem template={tmpl} selected={selectedTemplate && selectedTemplate.settingsId === tmpl.settingsId} deleteHandler={deleteHandler} />
                        </TableCell>
                    </TableRow>
                    )}
                    <TemplateCreate handleTemplateCreated={templateCreatedHandler}></TemplateCreate>
                </TableBody>
            </Table>
        </TableContainer>
    )
}