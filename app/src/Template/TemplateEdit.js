import { useEffect, useState } from 'react'
import { Button, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField } from '@material-ui/core';
import templateService from '../services/template.service';

export default function TemplateEdit(props) {
    const [settings, setSettings] = useState()


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
    const handleSettingChange = (e, key) => {
        console.log('e.target.value ' + e.target.value)
        console.log('key ' + key)
        debugger;
        if(settings[e.target.value]) {
            // alert("setting taken" + e.target.value);
            console.log('setting taken...')
            setSettings({...settings});
        } else {
            console.log('updating setting name...')
            
            // new key name and fetch current key value
            const newSetting = { [e.target.value] : settings[key] };

            // delete current key
            delete settings[key];

            setSettings({
                ...settings,
                ...newSetting
            })
        }
        
    }
    const handleValChange = (e, key) => {
        settings[key] = e.target.value;
        setSettings({
            ...settings,
        })
    }
    
    const saveTemplate = () => {
        let template = props.template;
        template.templateSettings = settings;
        templateService.updateTemplate(template).then(res => {
            setSettings(res.data.template.templateSettings)
        })
    }
    return (
        <TableContainer component={Paper} className="template-list">
            <h2>Template Settings</h2>
            <Table border={1}>
                <TableHead>
                    <TableRow>
                        <TableCell>
                            Setting Name
                        </TableCell>
                        <TableCell>
                            Default Value
                        </TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {settings && Object.entries(settings).map(([key, val], i) => 
                        <TableRow key={key}>
                            <TableCell>
                                <TextField 
                                    fullWidth 
                                    defaultValue={key} 
                                    onBlur={(e) => handleSettingChange(e, key)}
                                ></TextField>
                            </TableCell>
                            <TableCell>
                                <TextField 
                                    fullWidth 
                                    defaultValue={val}
                                    onBlur={(e) => handleValChange(e, key)}
                                ></TextField>
                            </TableCell>
                        </TableRow>
                    )}
                    <TableRow>
                        <TableCell colSpan={2} onClick={addRow}>
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
                    </TableRow>
                    <TableRow>
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
                </TableBody>
            </Table>
        </TableContainer>
    )
}