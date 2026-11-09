import java.util.Scanner;

public class Main {
    static class Role {
        private String name;
        private String[] permissions;

        public Role(String name, String[] permissions) {
            this.name = name;
            this.permissions = permissions;
        }

        public String getName() {
            return name;
        }

        public boolean hasPermission(String requestedPermission) {
            for (String permission : permissions) {
                if (permission.equalsIgnoreCase(requestedPermission)) {
                    return true;
                }
            }
            return false;
        }
    }

    static class User {
        private String username;
        private Role role;

        public User(String username, Role role) {
            this.username = username;
            this.role = role;
        }

        public String getUsername() {
            return username;
        }

        public Role getRole() {
            return role;
        }
    }

    static class RBACSystem {
        private Role[] roles;
        private User[] users;
        private int userCount;
        private String[] permissions = {
                "Create", "View", "Update", "Delete"
        };

        public RBACSystem() {
            roles = new Role[3];
            roles[0] = new Role("Admin", new String[] { "Create", "View", "Update", "Delete" });
            roles[1] = new Role("Manager", new String[] { "View", "Update" });
            roles[2] = new Role("Customer", new String[] { "View" });
            users = new User[100];
            userCount = 0;
        }

        public void registerUser(String username, int roleChoice) {
            if (findUser(username) != null) {
                System.out.println("This username is already registered");
                return;
            }
            if (userCount >= users.length) {
                System.out.println("Registration limit reached");
                return;
            }
            Role selectedRole = roles[roleChoice - 1];
            users[userCount] = new User(username, selectedRole);
            userCount++;
            System.out.println("Registration successful");
            System.out.println("Assigned role: " + selectedRole.getName());
        }

        private User findUser(String username) {
            for (int i = 0; i < userCount; i++) {
                if (users[i].getUsername().equalsIgnoreCase(username)) {
                    return users[i];
                }
            }
            return null;
        }

        public boolean isRegistered(String username) {
            return findUser(username) != null;
        }

        public void requestAccess(String username, int permissionChoice) {
            User user = findUser(username);
            if (user == null) {
                System.out.println("User not found, Please register first");
                return;
            }
            String requestedPermission = permissions[permissionChoice - 1];
            Role userRole = user.getRole();
            System.out.println("User role: " + userRole.getName());
            System.out.println("Requested resource: Account");
            System.out.println("Requested permission: " + requestedPermission);
            if (userRole.hasPermission(requestedPermission)) {
                System.out.println("Access granted");
            } else {
                System.out.println("Access denied");
                System.out.println("Your role does not have " + requestedPermission + " permission");
            }
        }

        public void displayRoles() {
            System.out.println("\nChoose a role:");

            for (int i = 0; i < roles.length; i++) {
                System.out.println((i + 1) + ". " + roles[i].getName());
            }
        }

        public void displayPermissions() {
            System.out.println("\nChoose an access request:");
            for (int i = 0; i < permissions.length; i++) {
                System.out.println((i + 1) + ". " + permissions[i]);
            }
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        RBACSystem system = new RBACSystem();
        int choice;
        do {
            System.out.println("\nSimple RBAC System");
            System.out.println("1. Register as a user");
            System.out.println("2. Request access to an account");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = readChoice(input);
            switch (choice) {
                case 1:
                    System.out.print("Enter your username: ");
                    String username = input.nextLine();
                    system.displayRoles();
                    System.out.print("Enter your choice: ");
                    int roleChoice = readChoice(input);
                    if (roleChoice >= 1 && roleChoice <= 3) {
                        system.registerUser(username, roleChoice);
                    } else {
                        System.out.println("Invalid role choice");
                    }
                    break;
                case 2:
                    System.out.print("Enter your username: ");
                    String accessUsername = input.nextLine();
                    if (!system.isRegistered(accessUsername)) {
                        System.out.println("User not found. Please register first");
                        break;
                    }
                    system.displayPermissions();
                    System.out.print("Enter your choice: ");
                    int permissionChoice = readChoice(input);
                    if (permissionChoice >= 1 && permissionChoice <= 4) {
                        system.requestAccess(accessUsername, permissionChoice);
                    } else {
                        System.out.println("Invalid permission choice");
                    }
                    break;
                case 3:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 3);
        input.close();
    }

    public static int readChoice(Scanner input) {
        try {
            return Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}