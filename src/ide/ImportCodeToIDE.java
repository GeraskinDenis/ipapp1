package ide;

import org.apache.commons.io.FilenameUtils;
import ru.ip.server.database.HibernateUtil;
import ru.ip.server.integration.ide.*;
import ru.ip.server.security.SessionContextHolder;
import ru.ip.server.utils.ContextUtils;

import java.io.File;
import java.io.IOException;

/**
 * Экспорт библиотек классов и правил в разрезе сущностей в текущий проект
 */
public class ImportCodeToIDE {

    public static void main(String[] args) throws IOException {

        //Путь до исходного кода проекта
        String projectSrcPath = getProjectSrcPath();

        // Подключение к пространству 89
        HibernateUtil.startSystemFactory();
        SessionContextHolder.setSystemContext(89);

        // Экспорт ButtonLibrary
        ButtonRuleExportService buttonLibraryExportService = new ButtonRuleExportService();
        buttonLibraryExportService.exportCodeToDirectory(ContextUtils.getWorkspaceId(), projectSrcPath);

        // Экспорт ClassLibrary
        ClassLibraryExportService classLibraryExportService = new ClassLibraryExportService();
        classLibraryExportService.exportCodeToDirectory(ContextUtils.getWorkspaceId(), projectSrcPath);

        // Экспорт сущностей и рулов к ним
        EntityRuleExportService typeRuleExportService = new EntityTypeRuleExportService();
        typeRuleExportService.exportCodeToDirectory(ContextUtils.getWorkspaceId(), projectSrcPath);

        // Экспорт
        EntityTypeExportService entityTypeExportService = new EntityTypeExportService();
        entityTypeExportService.exportCodeToDirectory(ContextUtils.getWorkspaceId(), projectSrcPath);

        //
        EntityTypeRuleExportService entityTypeRuleExportService = new EntityTypeRuleExportService();
        entityTypeRuleExportService.exportCodeToDirectory(ContextUtils.getWorkspaceId(), projectSrcPath);

        //
        WorkFlowTransitionRulesExportService workFlowTransitionRulesExportService = new WorkFlowTransitionRulesExportService();
        workFlowTransitionRulesExportService.exportCodeToDirectory(ContextUtils.getWorkspaceId(), projectSrcPath);

    }

    private static String getProjectSrcPath() {
        File currentDirFile = new File(".");
        String helper = currentDirFile.getAbsolutePath();
        return FilenameUtils.concat(helper.substring(0, helper.length() -1), "src");
    }
}
