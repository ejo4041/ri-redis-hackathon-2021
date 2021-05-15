import { useEffect, useState } from 'react'
import { Box, Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Tooltip } from '@material-ui/core';
import templateService from '../services/template.service';
import { DeleteForever } from '@material-ui/icons';
import SharedDialog from '../SharedComponents/SharedDialog';

export default function TemplateEdit(props) {
    const [settings, setSettings] = useState();
    const [openDialog, setOpenDialog] = useState(false);
    const [openCreateDialog, setOpenCreateDialog] = useState(false);
    const [newSettingKey, setNewSettingKey] = useState();
    const [deleteSettingKey, setDeleteSettingKey] = useState();
    const [settingsSavedMessage, setSettingsSavedMessage] = useState("");

    const handleClickOpen = (key) => {
        setDeleteSettingKey(key);
        setOpenDialog(true);
      };
    
      const handleClose = () => {
        setOpenDialog(false);
      };
      const handleCloseCreateDialog = () => {
        setOpenCreateDialog(false);
      };
      const handleDelete = () => {
          templateService.deleteTemplateSetting(props.template.settingsId, deleteSettingKey).then(res => {
            templateService.getTemplate(props.template.settingsId).then(res => {
                setSettings(res.data.template.templateSettings);
                handleClose()
            })
          })
      }


    useEffect(() => {
        if(props && props.template && props.template.templateSettings) {
            debugger;
            setSettings(props.template.templateSettings);
        } else {
            setSettings({'default': 'value'})
        }
    }, [setSettings, props])

    const addRow = () => {
        setSettings({
            ...settings,
            'default': 'value'
        })
    }
    const handleNewSettingChange = (e) => {
        setNewSettingKey(e.target.value)
    }
    const handleCreateSetting = () => {
        if(!settings[newSettingKey]) {
            settings[newSettingKey] = '';
            setSettings({
                ...settings
            })
            saveTemplate();
            handleCloseCreateDialog();
        }
    }
    const handleValChange = (e, key) => {
        settings[key] = e.target.value;
        setSettings({
            ...settings
        })
    }
    
    const saveTemplate = () => {
        let template = props.template;
        template.templateSettings = settings;
        templateService.updateTemplate(template).then(res => {
            setSettings(res.data.template.templateSettings);
            setSettingsSavedMessage("Settings Saved!");
            setTimeout(() => {
                setSettingsSavedMessage("")
            }, 3000);
        })
    }
    return (
        <>
            <TableContainer component={Paper} className="template-list">
                <h2>{props.template.templateName} Template Settings</h2>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>
                                Setting Name
                            </TableCell>
                            <TableCell colSpan={2}>
                                Default Value
                            </TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {settings && Object.entries(settings).map(([key, val], i) => 
                            <TableRow key={key}>
                                <TableCell style={{width: "50%"}}>
                                    {key}
                                </TableCell>
                                <TableCell style={{width: "50%"}}>
                                    <TextField 
                                        fullWidth 
                                        defaultValue={val}
                                        onBlur={(e) => handleValChange(e, key)}
                                    ></TextField>
                                </TableCell>
                                <TableCell>
                                    <Tooltip title="Delete Setting" placement="top">
                                        <Button 
                                            variant="contained" 
                                            color="primary" 
                                            disableElevation
                                            onClick={() => handleClickOpen(key)}
                                        >
                                            <DeleteForever />
                                        </Button>
                                    </Tooltip>
                                </TableCell>
                            </TableRow>
                        )}
                        <TableRow>
                            <TableCell colSpan={1} onClick={() => setOpenCreateDialog(true)}>
                                <Button 
                                    fullWidth 
                                    variant="contained" 
                                    color="default" 
                                    float="right" 
                                    disableElevation
                                >
                                    + New Setting
                                </Button>
                            </TableCell>
                            <TableCell colSpan={2} onClick={saveTemplate}>
                                <Button 
                                    fullWidth 
                                    variant="contained" 
                                    color="primary" 
                                    disableElevation
                                >
                                    Save Template Settings
                                </Button>
                            </TableCell>
                        </TableRow>
                        {settingsSavedMessage && 
                            <TableRow>
                                <TableCell colSpan={3}>
                                    <Box color="green">{settingsSavedMessage}</Box>
                                </TableCell>
                            </TableRow>
                        }
                    </TableBody>
                </Table>
            </TableContainer>
            
            <SharedDialog
                open={openDialog}
                onClose={handleClose}
                title={`Deleting "${deleteSettingKey}"`}
                body={`Are you sure you want to delete "${deleteSettingKey}", FOREVER?`}
                onCancel={handleClose}
                cancelText={'Cancel'}
                onAccept={handleDelete}
                acceptText={'Delete'}
            />
            
            <SharedDialog
                open={openCreateDialog}
                onClose={handleCloseCreateDialog}
                title={`Name your setting`}
                body={
                <>
                Please give your new setting a name:<br />
                <TextField 
                    fullWidth
                    onChange={(e) => handleNewSettingChange(e)}
                ></TextField>
                </>}
                onCancel={handleCloseCreateDialog}
                cancelText={'Cancel'}
                onAccept={handleCreateSetting}
                acceptText={'Add Setting'}
            />
        </>
    )
}