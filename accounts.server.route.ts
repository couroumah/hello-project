 .get("/:accountId", sessionMiddleware, async (c) => {
    const databases = c.get("databases");
    const currentUser = c.get("user");

    const { accountId } = c.req.param();

    const accountData = await databases.getDocument<Account>(
      DATABASE_ID,
      ACCOUNTS_ID,
      accountId
    );

    const currentMember = await getMember({
      databases,
      workspaceId: accountData.workspaceId,
      userId: currentUser.$id,
    });

    if (!currentMember) {
      return c.json({ error: "Unautorized" }, 401);
    }

    const transactions = await databases.listDocuments<Transaction>(
      DATABASE_ID,
      TRANSACTIONS_ID,
      [Query.equal("accountId", accountData.$id)]
    );

    const income = transactions.documents.reduce((acc, transaction) => {
      if (Number(transaction.amount) > 0) {
        acc += Number(transaction.amount);
      }
      return acc;
    }, 0);

    const expenses = transactions.documents.reduce((acc, transaction) => {
      if (Number(transaction.amount) < 0) {
        acc += Number(transaction.amount);
      }
      return acc;
    }, 0);

    const currentBalance = income + expenses;

    // sort transactions by date such that the most recent transaction is first
    const allTransactions = [...transactions].sort(
      (a, b) => new Date(b.dueDate).getTime() - new Date(a.dueDate).getTime()
    );

    const account = {
      id: accountData.$id,
      currentBalance: currentBalance,
      institutionId: 0,
      name: accountData.name,
      officialName: accountData.name,
      mask: "1234",
    };

    return c.json({
      data: account,
      transactions: allTransactions,
    });
  })
