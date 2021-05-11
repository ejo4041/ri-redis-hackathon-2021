import { useEffect, useState } from 'react'
import { Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField } from '@material-ui/core';

export default function TemplateEdit(props) {
    const [settings, setSettings] = useState()


    useEffect(() => {
        if(props && props.template && props.template.templateSettings) {
            debugger;
            setSettings(props.template.templateSettings);
        }
    }, [setSettings, props])

    const addRow = () => {
        setSettings({
            ...settings,
            'default': 'value'
        })
    }
    return (
        <TableContainer component={Paper} className="template-list">
            <h2>Template Settings</h2>
            <Table>
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
                        <TableRow key={i}>
                            <TableCell>
                                <TextField defaultValue={key}>asdf</TextField>
                            </TableCell>
                            <TableCell>
                                <TextField defaultValue={val}>asdf</TextField>
                            </TableCell>
                        </TableRow>
                    )}
                    <TableRow>
                        <TableCell colSpan={2} onClick={addRow}>
                            + Add Row
                        </TableCell>
                    </TableRow>
                </TableBody>
            </Table>
        </TableContainer>
    )
}