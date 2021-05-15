import { Box } from '@material-ui/core';
import { useState } from 'react';
import TemplateEdit from './TemplateEdit';
import TemplateList from './TemplateList'

export default function Template() {
    console.log("Template");
    const [template, setTemplate] = useState(null)
    const templateChangeHandler = (tmpl) => {
        setTemplate(tmpl);
    }
    return (
        <Box display="flex" width={1} p={1}>
            <Box flexGrow={1} m={2}>
                <TemplateList templateSelected={templateChangeHandler}/>
            </Box>
            <Box flexGrow={2} m={2}>
                {template && <TemplateEdit template={template} />}
            </Box>
        </Box>
    )
}