package org.openmrs.module.mirebalais;

import org.codehaus.jackson.node.ObjectNode;
import org.junit.Test;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.domain.Extension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CustomAppLoaderTest {

    @Test
    public void shouldSetUpAppsAndExtensions() throws Exception {
        // TODO implement--right now we are just testing to make sure there are no runtime tests
        CustomAppLoaderFactory factory = new CustomAppLoaderFactory();
        factory.getExtensions();
        factory.getAppDescriptors();
    }

    @Test
    public void shouldCreateHeader() {
        Extension extension = CustomAppLoaderFactory.header("id","logo");

        assertThat(extension.getId(), is("id"));
        assertThat(extension.getExtensionPointId(), is(CustomAppLoaderFactory.HEADER_EXTENSION_POINT));
        assertThat(extension.getType(), is("config"));
        assertThat((String) extension.getExtensionParams().get("logo-icon-url"), is("logo"));
    }

    @Test
    public void shouldCreateApp() {
        AppDescriptor app = CustomAppLoaderFactory.app("id", "label", "icon", "url", "privilege", null);

        assertThat(app.getId(), is("id"));
        assertThat(app.getLabel(), is("label"));
        assertThat(app.getIcon(), is("icon"));
        assertThat(app.getUrl(), is("url"));
        assertThat(app.getRequiredPrivilege(), is("privilege"));
    }

    @Test
    public void shouldCreatePatientTemplateApp() {
        AppDescriptor app = CustomAppLoaderFactory.findPatientTemplateApp("id", "label", "icon", "privilege", "afterSelectedUrl",
                CustomAppLoaderFactory.arrayNode(CustomAppLoaderFactory.objectNode("label", "label1", "link", "link1"),
                        CustomAppLoaderFactory.objectNode("label", "label2", "link", "link2")));

        assertThat(app.getId(), is("id"));
        assertThat(app.getLabel(), is("label"));
        assertThat(app.getIcon(), is("icon"));
        assertThat(app.getUrl(), is("coreapps/findpatient/findPatient.page?app=id"));
        assertThat(app.getRequiredPrivilege(), is("privilege"));
        assertThat(app.getConfig().get("afterSelectedUrl").getTextValue(), is("afterSelectedUrl"));
        assertThat(app.getConfig().get("label").getTextValue(), is("label"));
        assertThat(app.getConfig().get("heading").getTextValue(), is("label"));
        assertThat(app.getConfig().get("showLastViewedPatients").getBooleanValue(), is(false));
        assertThat(app.getConfig().get("breadcrumbs").get(0).get("label").getTextValue(), is("label1"));
        assertThat(app.getConfig().get("breadcrumbs").get(0).get("link").getTextValue(), is("link1"));
        assertThat(app.getConfig().get("breadcrumbs").get(1).get("label").getTextValue(), is("label2"));
        assertThat(app.getConfig().get("breadcrumbs").get(1).get("link").getTextValue(), is("link2"));

    }

    @Test
    public void shouldAddAppToHomePage() {
        AppDescriptor app = CustomAppLoaderFactory.app("id", "label", "icon", "url", "privilege", null);

        CustomAppLoaderFactory.addToHomePage(app);
        assertThat(app.getExtensions().size(), is(1));
        assertThat(app.getExtensions().get(0).getId(), is("id.appLink"));
        assertThat(app.getExtensions().get(0).getType(), is("link"));
        assertThat(app.getExtensions().get(0).getLabel(), is("label"));
        assertThat(app.getExtensions().get(0).getUrl(), is("url"));
        assertThat(app.getExtensions().get(0).getIcon(), is("icon"));
        assertThat(app.getExtensions().get(0).getRequiredPrivilege(), is("privilege"));
        assertThat(app.getExtensions().get(0).getExtensionPointId(), is(CustomAppLoaderFactory.HOME_PAGE_EXTENSION_POINT));
    }

    @Test
    public void shouldAddAppToSystemAdministrationPage() {
        AppDescriptor app = CustomAppLoaderFactory.app("id", "label", "icon", "url", "privilege", null);

        CustomAppLoaderFactory.addToSystemAdministrationPage(app);
        assertThat(app.getExtensions().size(), is(1));
        assertThat(app.getExtensions().get(0).getId(), is("id.systemAdministration.appLink"));
        assertThat(app.getExtensions().get(0).getType(), is("link"));
        assertThat(app.getExtensions().get(0).getLabel(), is("label"));
        assertThat(app.getExtensions().get(0).getUrl(), is("url"));
        assertThat(app.getExtensions().get(0).getIcon(), is("icon"));
        assertThat(app.getExtensions().get(0).getRequiredPrivilege(), is("privilege"));
        assertThat(app.getExtensions().get(0).getExtensionPointId(), is(CustomAppLoaderFactory.SYSTEM_ADMINISTRATION_PAGE_EXTENSION_POINT));
    }


    @Test
    public void shouldCreateVisitActionsExtension() {
        Extension extension = CustomAppLoaderFactory.visitAction(CustomAppLoaderFactory.ORDER_XRAY_VISIT_ACTION, "label", "icon","link", "url", "privilege", "require");

        assertThat(extension.getId(), is(CustomAppLoaderFactory.ORDER_XRAY_VISIT_ACTION));
        assertThat(extension.getLabel(), is("label"));
        assertThat(extension.getIcon(), is("icon"));
        assertThat(extension.getUrl(), is("url"));
        assertThat(extension.getScript(), nullValue());
        assertThat(extension.getRequiredPrivilege(), is("privilege"));
        assertThat(extension.getRequire(), is("require"));
        assertThat(extension.getType(), is("link"));
        assertThat(extension.getExtensionPointId(), is(CustomAppLoaderFactory.VISIT_ACTIONS_EXTENSION_POINT));
    }

    @Test
    public void shouldCreateOverallActionsExtension() {
        Extension extension = CustomAppLoaderFactory.overallAction(CustomAppLoaderFactory.PRINT_PAPER_FORM_LABEL_OVERALL_ACTION, "label", "icon","script", "script", "privilege", "require");

        assertThat(extension.getId(), is(CustomAppLoaderFactory.PRINT_PAPER_FORM_LABEL_OVERALL_ACTION));
        assertThat(extension.getLabel(), is("label"));
        assertThat(extension.getIcon(), is("icon"));
        assertThat(extension.getUrl(), is(nullValue()));
        assertThat(extension.getScript(), is("script"));
        assertThat(extension.getRequiredPrivilege(), is("privilege"));
        assertThat(extension.getRequire(), is("require"));
        assertThat(extension.getType(), is("script"));
        assertThat(extension.getExtensionPointId(), is(CustomAppLoaderFactory.OVERALL_ACTIONS_EXTENSION_POINT));
    }

    @Test
    public void shouldCreateAwaitingAdmissionActionsExtension() {
        Extension extension = CustomAppLoaderFactory.awaitingAdmissionAction(CustomAppLoaderFactory.ADMISSION_FORM_AWAITING_ADMISSION_ACTION, "label", "icon", "link", "url", "privilege", "require");

        assertThat(extension.getId(), is(CustomAppLoaderFactory.ADMISSION_FORM_AWAITING_ADMISSION_ACTION));
        assertThat(extension.getLabel(), is("label"));
        assertThat(extension.getIcon(), is("icon"));
        assertThat(extension.getUrl(), is("url"));
        assertThat(extension.getScript(), nullValue());
        assertThat(extension.getRequiredPrivilege(), is("privilege"));
        assertThat(extension.getRequire(), is("require"));
        assertThat(extension.getType(), is("link"));
        assertThat(extension.getExtensionPointId(), is(CustomAppLoaderFactory.AWAITING_ADMISSION_ACTIONS_EXTENSION_POINT));
    }


    @Test
    public void shouldCreateDashboardTab() {
        Extension extension = CustomAppLoaderFactory.dashboardTab("id", "label", "privilege", "provider", "fragment");

        assertThat(extension.getId(), is("id"));
        assertThat(extension.getExtensionPointId(), is("patientDashboard.tabs"));
        assertThat(extension.getType(), is("link"));
        assertThat(extension.getLabel(), is("label"));
        assertThat(extension.getRequiredPrivilege(), is("privilege"));
        assertThat((String) extension.getExtensionParams().get("provider"), is("provider"));
        assertThat((String) extension.getExtensionParams().get("fragment"), is("fragment"));
    }

    @Test
    public void shouldCreateFragmentExtension() {
        Extension extension = CustomAppLoaderFactory.fragmentExtension("id", "provider", "fragment", "privilege", "extensionPoint");

        assertThat(extension.getId(), is ("id"));
        assertThat((String) extension.getExtensionParams().get("provider"), is("provider"));
        assertThat((String) extension.getExtensionParams().get("fragment"), is("fragment"));
        assertThat(extension.getRequiredPrivilege(), is("privilege"));
        assertThat(extension.getExtensionPointId(), is("extensionPoint"));
    }

    @Test
    public void shouldCreateAppExtension() {
        AppDescriptor app = new AppDescriptor();
        CustomAppLoaderFactory.appExtension(app, "id", "label", "icon", "type",
                "url", "requiredPrivilege", 1, "extensionPoint");

        assertThat(app.getExtensions().size(), is(1));
        assertThat(app.getExtensions().get(0).getId(), is("id"));
        assertThat(app.getExtensions().get(0).getExtensionPointId(), is("extensionPoint"));
        assertThat(app.getExtensions().get(0).getType(), is("type"));
        assertThat(app.getExtensions().get(0).getLabel(), is("label"));
        assertThat(app.getExtensions().get(0).getUrl(), is("url"));
        assertThat(app.getExtensions().get(0).getIcon(), is("icon"));
        assertThat(app.getExtensions().get(0).getOrder(), is(1));
        assertThat(app.getExtensions().get(0).getRequiredPrivilege(), is("requiredPrivilege"));
    }


    @Test
    public void shouldCreateEncounterTemplateExtension() {
        Extension  extension = CustomAppLoaderFactory.encounterTemplate("id", "provider", "fragment");

        assertThat(extension.getId(), is("id"));
        assertThat(extension.getExtensionPointId(), is("org.openmrs.referenceapplication.encounterTemplate"));
        assertThat(extension.getType(), is("fragment"));
        assertThat((String) extension.getExtensionParams().get("templateId"), is("id"));
        assertThat((String) extension.getExtensionParams().get("templateFragmentProviderName"), is("provider"));
        assertThat((String) extension.getExtensionParams().get("templateFragmentId"), is("fragment"));
        assertThat((String) extension.getExtensionParams().get("templateFragmentProviderName"), is("provider"));
    }

    @Test
    public void shouldRegisterTemplateForEncounterType() {

        List<Extension> extensions = new ArrayList<Extension>();
        Extension template = CustomAppLoaderFactory.encounterTemplate("id", "provider", "fragment");
        extensions.add(template);

        CustomAppLoaderFactory factory = new CustomAppLoaderFactory();
        factory.setExtensions(extensions);

        factory.registerTemplateForEncounterType("encounterTypeUuid", factory.findExtensionById("id"), "icon",
                true, false, "primaryEncounterRoleUuid");

        assertTrue(template.getExtensionParams().containsKey("supportedEncounterTypes"));
        assertTrue(((Map<String, Object>) template.getExtensionParams().get("supportedEncounterTypes")).containsKey("encounterTypeUuid"));

        Map<String,Object> params = (Map<String, Object>) ((Map<String, Object>) template.getExtensionParams().get("supportedEncounterTypes")).get("encounterTypeUuid");
        assertThat((String) params.get("icon"), is("icon"));
        assertThat((String) params.get("primaryEncounterRoleUuid"), is("primaryEncounterRoleUuid"));
        assertThat((Boolean) params.get("displayWithHtmlForm"), is(true));
        assertThat((Boolean) params.get("editable"), is(false));

    }

    @Test
    public void shouldConvertToObjectNode() {
        ObjectNode objectNode = CustomAppLoaderFactory.objectNode("int", 1, "string", "string", "boolean", true);
        assertThat(objectNode.get("int").getIntValue(), is(1));
        assertThat(objectNode.get("string").getTextValue(), is("string"));
        assertThat(objectNode.get("boolean").getBooleanValue(), is(true));
    }

    @Test
    public void shouldConvertToMap() {
        Map<String,Object> map = CustomAppLoaderFactory.map("int", 1, "string", "string", "boolean", true);
        assertThat((Integer) map.get("int"), is (1));
        assertThat((String) map.get("string"), is ("string"));
        assertThat((Boolean) map.get("boolean"), is (true));
    }

}
