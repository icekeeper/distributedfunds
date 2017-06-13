jQuery.post("/api/front/user", JSON.stringify({login:'icekeeper',name:'Alex' }))
jQuery.post("/api/front/user", JSON.stringify({login:'terry',name:'Danila' }))
jQuery.post("/api/front/user", JSON.stringify({login:'ortemij',name:'Artem' }))
jQuery.post("/api/front/user", JSON.stringify({login:'40in',name:'Evgeniy' }))
jQuery.post("/api/front/user", JSON.stringify({login:'hexode',name:'Alexander' }))

jQuery.post("/api/front/fund", JSON.stringify({name:"Test Fund", description:"Test Fund 1", supervisorId: 1, userIds: [1,2,3,4,5]}))

jQuery.post("/api/front/fund/1/transaction", JSON.stringify(
    {
        fundId: 1,
        amount: 1000,
        description: "Test transaction 1",
        shares: [
            { userId: 1, amount: 800 },
            { userId: 2, amount: -200 },
            { userId: 3, amount: -200 },
            { userId: 4, amount: -200 },
            { userId: 5, amount: -200 }
        ]
    }
))

jQuery.post("/api/front/fund/1/transaction", JSON.stringify(
    {
        fundId: 1,
        amount: 1000,
        description: "Test transaction 2",
        shares: [
            { userId: 1, amount: 800 },
            { userId: 2, amount: -200 },
            { userId: 3, amount: -200 },
            { userId: 4, amount: -200 },
            { userId: 5, amount: -200 }
        ]
    }
))

jQuery.post("/api/front/fund/1/transaction", JSON.stringify(
    {
        fundId: 1,
        amount: 1000,
        description: "Test transaction 3",
        shares: [
            { userId: 1, amount: 800 },
            { userId: 2, amount: -200 },
            { userId: 3, amount: -200 },
            { userId: 4, amount: -200 },
            { userId: 5, amount: -200 }
        ]
    }
))

jQuery.post("/api/front/fund/1/transaction", JSON.stringify(
    {
        fundId: 1,
        amount: 1000,
        description: "Test transaction 4",
        shares: [
            { userId: 1, amount: 800 },
            { userId: 2, amount: -200 },
            { userId: 3, amount: -200 },
            { userId: 4, amount: -200 },
            { userId: 5, amount: -200 }
        ]
    }
))

jQuery.post("/api/front/fund/1/transaction", JSON.stringify(
    {
        fundId: 1,
        amount: 1000,
        description: "Test transaction 5",
        shares: [
            { userId: 1, amount: 800 },
            { userId: 2, amount: -200 },
            { userId: 3, amount: -200 },
            { userId: 4, amount: -200 },
            { userId: 5, amount: -200 }
        ]
    }
))