package es.upm.miw.security;

public final class Security {
    public static final String AUTHENTICATED = "isAuthenticated()";
    public static final String ADMIN = "hasAnyRole('admin')";
    public static final String ADMIN_MANAGER = "hasAnyRole('admin','manager')";
    public static final String ADMIN_MANAGER_OPERATOR = "hasAnyRole('admin','manager','operator')";
    public static final String ADMIN_MANAGER_LAWYER = "hasAnyRole('admin','manager','lawyer')";
    public static final String ADMIN_MANAGER_LAWYER_OPERATOR = "hasAnyRole('admin','manager','lawyer','operator')";
    public static final String ADMIN_MANAGER_LAWYER_OPERATOR_CUSTOMER = "hasAnyRole('admin','manager','lawyer','operator','customer')";
    public static final String ADMIN_MANAGER_LAWYER_OPERATOR_URL_TOKEN = "hasAnyRole('admin','manager','lawyer','operator','url_token')";
    public static final String URL_TOKEN = "hasAnyRole('url_token')";
    public static final String CUSTOMER_OWNER_MOBILE = "hasRole('customer') and #id == authentication.name";
    public static final String ALL = "permitAll()";
    public static final String OR = " or ";
    public static final String AND = " and ";

    private Security() {
        // Forbidden
    }
}
