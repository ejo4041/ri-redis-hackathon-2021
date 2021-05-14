import './TemplateListItem.scss'


export default function TemplateListItem(props) {
    // const handleClick = () => {
    //     props.templateSelected(props.template);
    // }
    return (
        <div key={props.template.templateName}>
            {props.template.templateName}
        </div>
    )
}