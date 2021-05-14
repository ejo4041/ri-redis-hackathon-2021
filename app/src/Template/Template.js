import { useState } from 'react';
import './Template.scss'
import TemplateEdit from './TemplateEdit';
import TemplateList from './TemplateList'

export default function Template() {
    console.log("Template");
    const [template, setTemplate] = useState(null)
    const templateChangeHandler = (tmpl) => {
        setTemplate(tmpl);
    }
    return (
        <div className="templates">
            <TemplateList templateSelected={templateChangeHandler}/>
            {template && <TemplateEdit template={template} />}
        </div>
    )
}