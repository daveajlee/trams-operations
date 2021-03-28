db.createUser(
        {
            user: "tramsOperations",
            pwd: "myTraMSpassword",
            roles: [
                {
                    role: "readWrite",
                    db: "tramsOperations"
                }
            ]
        }
);