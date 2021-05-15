import { Box, Button, Grid, TableCell, TableRow, TextField, Tooltip } from '@material-ui/core';
import { useState } from 'react';
import templateService from '../services/template.service';


export default function TemplateCreate(props) {
    const [creatingTemplate, setCreatingTemplate] = useState(false)
    const [newTemplateName, setNewTemplateName] = useState(false)
    const [displayError, setDisplayError] = useState(null)
    
    const createTemplate = () => {
        if (newTemplateName && newTemplateName.length > 0) {
            templateService.addTemplate({"templateName": newTemplateName}).then(res => {
                setNewTemplateName(null);
                setCreatingTemplate(null);
                if (res && res.data && res.data.template) {
                    props.handleTemplateCreated(res.data.template);
                }
            });
        } else {
            setDisplayError('Please enter a Template Name');
        }
    }
    
    const handleTemplateName = (e) => {
        setNewTemplateName(e.target.value);
        if(newTemplateName && newTemplateName.length > 0) {
            setDisplayError(null);
        }
    }
    return (
        
        <TableRow>
            {!creatingTemplate && <TableCell className="template-list-item" onClick={() => setCreatingTemplate(true)}>
                
                <Tooltip title="Add New Template" placement="right">
                    <Button 
                        fullWidth 
                        variant="contained" 
                        color="default" 
                        float="right" 
                        disableElevation
                    >
                        + New Template
                    </Button>
                </Tooltip>
            </TableCell>}
            {creatingTemplate && <TableCell>
                <Box display="flex" justifyContent="space-between" alignItems="center" flexWrap="nowrap">
                    <Box mr={2} flexGrow={1}>
                        <TextField 
                            fullWidth 
                            placeholder="New Template" 
                            onChange={handleTemplateName}
                            error={displayError}
                            helperText={displayError}
                        ></TextField>
                    </Box>
                    <Box flexGrow={0}>
                        <Button 
                            onClick={createTemplate} 
                            variant="contained" 
                            color="primary"
                            disableElevation
                        >
                            Create
                        </Button>
                    </Box>
                </Box>
            </TableCell>}
        </TableRow>
    )
}