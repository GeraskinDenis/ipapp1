package ide.utils;

import ru.ip.server.entity.EntityDTO;

import java.util.Map;

public class DtoUtils {
    public static void printEntityDtoFields(EntityDTO dto) {
        System.out.println("******************************************************************************************");
        Map<String, Object> fields = dto.getFieldList();
        fields.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    public static void printEntityDtoField(EntityDTO dto, String field) {
        System.out.println("******************************************************************************************");
        System.out.println(field + ": " + dto.getAsString(field));
    }
}
