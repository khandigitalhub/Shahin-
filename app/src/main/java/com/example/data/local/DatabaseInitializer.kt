package com.example.data.local

import com.example.data.model.AgentEntity
import com.example.data.model.AuditLogEntity
import com.example.data.model.CpaConversionEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.OfferEntity
import com.example.data.model.OrderEntity
import com.example.data.model.PayoutEntity
import com.example.data.model.ProductEntity
import com.example.data.model.TrainingModuleEntity

object DatabaseInitializer {

  suspend fun populateInitialDataIfEmpty(database: AppDatabase) {
    val productDao = database.productDao()
    val agentDao = database.agentDao()
    val orderDao = database.orderDao()
    val offerDao = database.offerDao()
    val notificationDao = database.notificationDao()
    val payoutDao = database.payoutDao()
    val trainingDao = database.trainingDao()
    val auditLogDao = database.auditLogDao()
    val cpaConversionDao = database.cpaConversionDao()

    if (productDao.count() == 0) {
      val sampleProducts = listOf(
        // Fashion & Clothing
        ProductEntity(
          title = "প্রিমিয়াম সুতি জামদানি শাড়ি",
          category = "ফ্যাশন ও পোশাক",
          wholesalePrice = 1150.0,
          retailPrice = 1650.0,
          baseCommission = 250.0,
          points = 50,
          stock = 38,
          description = "ট্রেডিশনাল সুতি মসলিন বডি এবং সোনালী জরি কাজের আচল। অত্যন্ত আরামদায়ক ও উৎসবের জন্য আকর্ষণীয়। কাস্টমারদের জন্য ১০০% সন্তুষ্টি গ্যারান্টি।",
          fabricOrMaterial = "প্রিমিয়াম সুতি ও রেশমি সুতা",
          sku = "SB-SR-01",
          isTrending = true
        ),
        ProductEntity(
          title = "পাকিস্তানি লন ভয়েল এমব্রয়ডারি থ্রি-পিস",
          category = "ফ্যাশন ও পোশাক",
          wholesalePrice = 1400.0,
          retailPrice = 1950.0,
          baseCommission = 300.0,
          points = 60,
          stock = 25,
          description = "ডিজিটাল প্রিন্টেড সুতি কামিজ, গলায় ঘন সুতার এম্ব্রয়ডারি কাজ এবং পিওর সিফন ওড়না। সেলাই ছাড়া আনস্টিচড ফেব্রিক।",
          fabricOrMaterial = "১০০% প্রিমিয়াম সুতি লন ও সিফন ওড়না",
          sku = "SB-TP-02",
          isTrending = true
        ),
        ProductEntity(
          title = "কম্বড কটন ড্রপ-শোল্ডার টি-শার্ট ও পোলো কম্বো",
          category = "ফ্যাশন ও পোশাক",
          wholesalePrice = 520.0,
          retailPrice = 850.0,
          baseCommission = 150.0,
          points = 30,
          stock = 60,
          description = "২২০ জিএসএম হেভিওয়েট কম্বড কটন টি-শার্ট। রঙ উঠবে না ও সুতা কুঁচকাবে না। জেন-জি ও তরুণদের জন্য স্টাইলিশ ক্যাজুয়াল ওয়্যার।",
          fabricOrMaterial = "১০০% প্রিমিয়াম সুতি কম্বড কটন",
          sku = "SB-TS-07",
          isTrending = true
        ),
        ProductEntity(
          title = "ক্লাসিক ফরমাল ফুল স্লিভ শার্ট ও ডেনিম প্যান্ট",
          category = "ফ্যাশন ও পোশাক",
          wholesalePrice = 980.0,
          retailPrice = 1550.0,
          baseCommission = 250.0,
          points = 45,
          stock = 32,
          description = "অফিস ও ফরমাল ইভেন্টের জন্য সেরা প্রিমিয়াম কটন শার্ট এবং স্ট্রেচেবল আরামদায়ক ডেনিম জিন্স। সাইজ ২৮ থেকে ৩৬ পর্যন্ত স্টক প্রস্তুত।",
          fabricOrMaterial = "অক্সফোর্ড কটন ফেব্রিক ও স্ট্রেচ ডেনিম",
          sku = "SB-SH-08",
          isTrending = false
        ),
        ProductEntity(
          title = "হোয়াইট ক্যাজুয়াল লেদার স্নিকার্স ও স্নিকার শু",
          category = "ফ্যাশন ও পোশাক",
          wholesalePrice = 750.0,
          retailPrice = 1290.0,
          baseCommission = 220.0,
          points = 40,
          stock = 40,
          description = "সফট মেমোরি ফোম ইনসোল যুক্ত আল্ট্রা-লাইটওয়েট ক্যাজুয়াল স্নিকার্স। সারাদিন পরে হাঁটলেও পায়ে ব্যথা হবে না।",
          fabricOrMaterial = "পিইউ সিন্থেটিক লেদার ও নন-স্লিপ রাবার সোল",
          sku = "SB-SH-09",
          isTrending = true
        ),
        ProductEntity(
          title = "হ্যান্ডক্রাফটেড প্রিমিয়াম লেদার পার্টি ব্যাগ ও ক্লাচ",
          category = "হ্যান্ডব্যাগ",
          wholesalePrice = 650.0,
          retailPrice = 1050.0,
          baseCommission = 180.0,
          points = 35,
          stock = 30,
          description = "স্মার্ট ডিজাইনের পার্টি ব্যাগ সাথে গোল্ডেন চেইন বেল্ট। মোবাইল, লিপস্টিক ও টাকা রাখার মাল্টিপল কম্পার্টমেন্ট।",
          fabricOrMaterial = "পিইউ ওয়াটারপ্রুফ লেদার ও মেটাল জিপার",
          sku = "SB-BG-05",
          isTrending = false
        ),

        // Food & Grocery
        ProductEntity(
          title = "সুন্দরবনের ১০০% প্রাকৃতিক চাকভাঙা মধু (৫০০ গ্রাম)",
          category = "ফুড ও গ্রোসারি",
          wholesalePrice = 480.0,
          retailPrice = 750.0,
          baseCommission = 130.0,
          points = 25,
          stock = 55,
          description = "কোনো প্রকার ভেজাল বা কেমিক্যাল ছাড়া খাঁটি সুন্দরবনের খলিশা ফুলের প্রাকৃতিক মধু। স্বাস্থ্য সুরক্ষা ও রোগ প্রতিরোধে অতুলনীয়।",
          fabricOrMaterial = "১০০% খাঁটি প্রাকৃতিক মধু",
          sku = "SB-FD-10",
          isTrending = true
        ),
        ProductEntity(
          title = "সিলেটের প্রিমিয়াম অর্থোডক্স ব্ল্যাক টি (৪০০ গ্রাম)",
          category = "ফুড ও গ্রোসারি",
          wholesalePrice = 280.0,
          retailPrice = 450.0,
          baseCommission = 80.0,
          points = 20,
          stock = 70,
          description = "সিলেটের শ্রীমঙ্গলের সেরা বাগানের সতেজ চা পাতার মনোমুগ্ধকর সুবাস ও কড়া লিকার। প্রতিদিনের পারিবারিক সতেজতার জন্য সেরা।",
          fabricOrMaterial = "১০০% ফ্রেশ অর্থোডক্স টি লিফ",
          sku = "SB-FD-11",
          isTrending = false
        ),
        ProductEntity(
          title = "প্রিমিয়াম রোস্টেড কাজু, পেস্তা ও আমন্ড ড্রাই ফ্রুটস কম্বো",
          category = "ফুড ও গ্রোসারি",
          wholesalePrice = 690.0,
          retailPrice = 1050.0,
          baseCommission = 170.0,
          points = 35,
          stock = 45,
          description = "কাজুবাদাম, কাঠবাদাম, পেস্তা বাদাম ও আফগান কিসমিসের পুষ্টিকর স্বাস্থ্যকর ড্রাই ফ্রুটস জিপার প্যাক। ১০০% সতেজ ও মচমচে।",
          fabricOrMaterial = "ইমপোর্টেড হাই-গ্রেড নাটস কম্বিনেশন",
          sku = "SB-FD-12",
          isTrending = true
        ),

        // Electronics & Gadgets
        ProductEntity(
          title = "ব্লুটুথ কলিং ও হেলথ ট্র্যাকার ওয়াটারপ্রুফ স্মার্টওয়াচ",
          category = "ইলেকট্রনিক্স ও গ্যাজেট",
          wholesalePrice = 1100.0,
          retailPrice = 1750.0,
          baseCommission = 280.0,
          points = 55,
          stock = 34,
          description = "১.৮৫ ইঞ্চি এইচডি ডিসপ্লে, ব্লুটুথ কলিং, হার্ট রেট ও ব্লাড অক্সিজেন ট্র্যাকার, ৭ দিনের শক্তিশালী ব্যাটারি ব্যাকআপ।",
          fabricOrMaterial = "জিঙ্ক অ্যালয় বডি ও সিলিকন স্ট্র্যাপ",
          sku = "SB-EL-13",
          isTrending = true
        ),
        ProductEntity(
          title = "নয়েজ ক্যানসেলিং ট্রু-ওয়্যারলেস ব্লুটুথ ইয়ারবাডস",
          category = "ইলেকট্রনিক্স ও গ্যাজেট",
          wholesalePrice = 780.0,
          retailPrice = 1290.0,
          baseCommission = 210.0,
          points = 40,
          stock = 48,
          description = "ডিপ বাস, অ্যাক্টিভ এনভায়রনমেন্টাল নয়েজ ক্যানসেলেশন (ENC), টাইপ-সি ফাস্ট চার্জিং ও ৩০ ঘণ্টার মিউজিক ব্যাকআপ।",
          fabricOrMaterial = "অ্যাবস পলিমার ও গোল্ড-প্লেটেড পিন",
          sku = "SB-EL-14",
          isTrending = true
        ),
        ProductEntity(
          title = "২০,০০০ mAh ফাস্ট চার্জিং ডুয়াল ইউএসবি পাওয়ার ব্যাংক",
          category = "ইলেকট্রনিক্স ও গ্যাজেট",
          wholesalePrice = 950.0,
          retailPrice = 1490.0,
          baseCommission = 230.0,
          points = 45,
          stock = 28,
          description = "২২.৫W সুপার ফাস্ট চার্জিং আউটপুট, এলইডি ডিজিটাল ডিসপ্লে ও মাল্টি-লেয়ার ব্যাটারি প্রোটেকশন সিস্টেম।",
          fabricOrMaterial = "ফায়ারপ্রুফ পলিকার্বনেট কেসিং",
          sku = "SB-EL-15",
          isTrending = false
        ),

        // Beauty & Cosmetics
        ProductEntity(
          title = "কোরিয়ান গ্লাস স্কিন ব্রাইটেনিং সিরাম ও ফেসওয়াশ কম্বো",
          category = "কসমেটিকস ও বিউটি",
          wholesalePrice = 850.0,
          retailPrice = 1350.0,
          baseCommission = 220.0,
          points = 45,
          stock = 42,
          description = "ত্বকের দাগ দূর করে প্রাকৃতিক উজ্জ্বলতা ও ময়েশ্চার প্রদান করে। কোনো পার্শ্বপ্রতিক্রিয়া ছাড়া সব ধরনের স্কিনের জন্য উপযোগী।",
          fabricOrMaterial = "নায়াসিনামাইড, হায়ালুরোনিক অ্যাসিড ও রাইস এক্সট্রাক্ট",
          sku = "SB-CS-04",
          isTrending = true
        ),
        ProductEntity(
          title = "কুন্দন গোল্ড প্লেটেড ব্রাইডাল জুয়েলারি সেট",
          category = "জুয়েলারি",
          wholesalePrice = 750.0,
          retailPrice = 1250.0,
          baseCommission = 200.0,
          points = 40,
          stock = 50,
          description = "রয়্যাল কুন্দন ও মুক্তার কম্বিনেশনে তৈরি প্রিমিয়াম নেকলেস, ম্যাচিং ঝুমকা ও টিকলি। রঙ সহজে নষ্ট হয় না।",
          fabricOrMaterial = "হাই-গ্রেড ব্রাস ও গোল্ড মাইক্রোপ্লেটিং",
          sku = "SB-JW-03",
          isTrending = true
        ),

        // Home & Lifestyle
        ProductEntity(
          title = "রিচার্জেবল পোর্টেবল ফ্রুট জুসার ও স্মুদি ব্লেন্ডার",
          category = "হোম ও লাইফস্টাইল",
          wholesalePrice = 620.0,
          retailPrice = 990.0,
          baseCommission = 160.0,
          points = 30,
          stock = 36,
          description = "ইউএসবি রিচার্জেবল ৬-ব্লেড ব্লেন্ডার। মাত্র ৩০ সেকেন্ডে যেকোনো তাজা ফলের জুস তৈরি করুন। অফিস বা ট্রাভেলের জন্য পারফেক্ট।",
          fabricOrMaterial = "ফুড গ্রেড পিসি মেটেরিয়াল ও স্টেইনলেস স্টিল ব্লেড",
          sku = "SB-HM-16",
          isTrending = true
        )
      )
      productDao.insertAll(sampleProducts)
    }

    if (agentDao.count() == 0) {
      val defaultAgents = listOf(
        AgentEntity(
          id = 1,
          name = "সুমাইয়া ইসলাম",
          phone = "01755123456",
          email = "sumaiya.agent@gmail.com",
          district = "ঢাকা",
          businessName = "সুমাইয়া বুটিক গ্যালারি",
          bkashNumber = "01755123456",
          nagadNumber = "01755123456",
          totalEarnings = 6350.0,
          availableBalance = 2450.0,
          pendingBalance = 750.0,
          totalOrders = 18,
          points = 320,
          badgeLevel = "স্টার রিসেলার",
          role = "AGENT",
          status = "ACTIVE",
          referralCode = "REF-SB01",
          cpaConversionsCount = 4,
          cpaEarnings = 420.0,
          cpsEarnings = 5930.0
        ),
        AgentEntity(
          id = 2,
          name = "আয়েশা খাতুন",
          phone = "01811223344",
          email = "ayesha.top@gmail.com",
          district = "চট্টগ্রাম",
          businessName = "আয়েশা ফ্যাশন পয়েন্ট",
          bkashNumber = "01811223344",
          nagadNumber = "",
          totalEarnings = 18750.0,
          availableBalance = 4200.0,
          pendingBalance = 1500.0,
          totalOrders = 45,
          points = 940,
          badgeLevel = "টপ সুপারস্টার",
          role = "AGENT",
          status = "ACTIVE",
          referralCode = "REF-SB02",
          cpaConversionsCount = 8,
          cpaEarnings = 880.0,
          cpsEarnings = 17870.0
        ),
        AgentEntity(
          id = 3,
          name = "সাদিয়া ইসলাম মিলি",
          phone = "01922334455",
          email = "sadia.mili@gmail.com",
          district = "রাজশাহী",
          businessName = "মিলি'স কালেকশন",
          bkashNumber = "01922334455",
          nagadNumber = "01922334455",
          totalEarnings = 15200.0,
          availableBalance = 3100.0,
          pendingBalance = 1200.0,
          totalOrders = 38,
          points = 760,
          badgeLevel = "গোল্ড রিসেলার",
          role = "AGENT",
          status = "ACTIVE",
          referralCode = "REF-SB03",
          cpaConversionsCount = 5,
          cpaEarnings = 520.0,
          cpsEarnings = 14680.0
        ),
        AgentEntity(
          id = 4,
          name = "তানজিলা আক্তার",
          phone = "01633445566",
          email = "tanjila.aktar@gmail.com",
          district = "সিলেট",
          businessName = "সুরমা বুটিক হাউস",
          bkashNumber = "01633445566",
          nagadNumber = "",
          totalEarnings = 12400.0,
          availableBalance = 2800.0,
          pendingBalance = 900.0,
          totalOrders = 32,
          points = 620,
          badgeLevel = "সিলভার স্টার",
          role = "AGENT",
          status = "ACTIVE",
          referralCode = "REF-SB04"
        ),
        AgentEntity(
          id = 5,
          name = "ফাহমিদা চৌধুরী",
          phone = "01544556677",
          email = "fahmida.bd@gmail.com",
          district = "খুলনা",
          businessName = "রূপসী বাংলা ফ্যাশন",
          bkashNumber = "01544556677",
          nagadNumber = "01544556677",
          totalEarnings = 11350.0,
          availableBalance = 1950.0,
          pendingBalance = 800.0,
          totalOrders = 28,
          points = 550,
          badgeLevel = "রাইজিং এন্টারপ্রেনার",
          role = "AGENT",
          status = "ACTIVE",
          referralCode = "REF-SB05"
        ),
        AgentEntity(
          id = 99,
          name = "Standard Business পরিচালক",
          phone = "01700000000",
          email = "admin@standardbusiness.com",
          district = "ঢাকা",
          businessName = "Standard Business হেডকোয়ার্টার",
          bkashNumber = "01700000000",
          nagadNumber = "01700000000",
          totalEarnings = 0.0,
          availableBalance = 0.0,
          pendingBalance = 0.0,
          totalOrders = 0,
          points = 0,
          badgeLevel = "ব্যবসা পরিচালক",
          role = "ADMIN",
          status = "ACTIVE",
          referralCode = "ADMIN-SB99"
        )
      )
      agentDao.insertAll(defaultAgents)
    }

    if (orderDao.count() == 0) {
      val now = System.currentTimeMillis()
      val day = 86400000L
      val sampleOrders = listOf(
        OrderEntity(
          orderNumber = "SB-2026-101",
          agentId = 1,
          agentName = "সুমাইয়া ইসলাম",
          productId = 1,
          productTitle = "প্রিমিয়াম সুতি জামদানি শাড়ি",
          quantity = 1,
          wholesalePrice = 1150.0,
          resellPrice = 1650.0,
          baseCommission = 250.0,
          agentProfit = 500.0,
          pointsEarned = 50,
          customerName = "ফারহানা ইয়াসমিন",
          customerPhone = "01711998877",
          customerAddress = "রোড #৪, বাড়ি #১২, ধানমন্ডি",
          district = "ঢাকা",
          deliveryArea = "ঢাকা সিটির ভেতরে",
          deliveryCharge = 60.0,
          codAmount = 1710.0,
          status = "ডেলিভারি সম্পন্ন",
          courierName = "Steadfast Courier",
          trackingNumber = "ST-994821",
          orderDate = now - (3 * day),
          deliveredDate = now - (1 * day),
          commissionSettled = true
        ),
        OrderEntity(
          orderNumber = "SB-2026-102",
          agentId = 1,
          agentName = "সুমাইয়া ইসলাম",
          productId = 2,
          productTitle = "পাকিস্তানি লন ভয়েল এমব্রয়ডারি থ্রি-পিস",
          quantity = 1,
          wholesalePrice = 1400.0,
          resellPrice = 1950.0,
          baseCommission = 300.0,
          agentProfit = 550.0,
          pointsEarned = 60,
          customerName = "রোকসানা পারভীন",
          customerPhone = "01822887766",
          customerAddress = "জিইসি মোড়, চকবাজার",
          district = "চট্টগ্রাম",
          deliveryArea = "ঢাকা সিটির বাইরে",
          deliveryCharge = 120.0,
          codAmount = 2070.0,
          status = "কুরিয়ারে হস্তান্তর",
          courierName = "RedX Logistics",
          trackingNumber = "RDX-883011",
          orderDate = now - (1 * day),
          commissionSettled = false
        ),
        OrderEntity(
          orderNumber = "SB-2026-103",
          agentId = 1,
          agentName = "সুমাইয়া ইসলাম",
          productId = 7,
          productTitle = "সুন্দরবনের ১০০% প্রাকৃতিক চাকভাঙা মধু (৫০০ গ্রাম)",
          quantity = 2,
          wholesalePrice = 480.0,
          resellPrice = 750.0,
          baseCommission = 130.0,
          agentProfit = 400.0,
          pointsEarned = 50,
          customerName = "নুসরাত জাহান",
          customerPhone = "01933776655",
          customerAddress = "হাউজ #২১, সেক্টর #৩, উত্তরা",
          district = "ঢাকা",
          deliveryArea = "ঢাকা সিটির ভেতরে",
          deliveryCharge = 60.0,
          codAmount = 1560.0,
          status = "প্রসেসিং",
          courierName = "Pathao Courier",
          trackingNumber = "PTH-10294",
          orderDate = now - (4 * 3600000L),
          commissionSettled = false
        )
      )
      orderDao.insertAll(sampleOrders)

      payoutDao.insertAll(
        listOf(
          PayoutEntity(
            agentId = 1,
            agentName = "সুমাইয়া ইসলাম",
            amount = 1200.0,
            method = "bKash",
            accountNumber = "01755123456",
            status = "পেমেন্ট সম্পন্ন",
            trxId = "BK84920152",
            requestedDate = now - (5 * day),
            processedDate = now - (4 * day)
          ),
          PayoutEntity(
            agentId = 1,
            agentName = "সুমাইয়া ইসলাম",
            amount = 800.0,
            method = "Nagad",
            accountNumber = "01755123456",
            status = "পেমেন্ট সম্পন্ন",
            trxId = "NG7721903",
            requestedDate = now - (2 * day),
            processedDate = now - (1 * day)
          )
        )
      )
    }

    if (offerDao.count() == 0) {
      val sampleOffers = listOf(
        // CPA Offer 1: App Install
        OfferEntity(
          title = "শপিং অ্যাপ ইনস্টল ও অ্যাকাউন্ট সাইনআপ",
          subtitle = "নতুন গ্রাহককে অ্যাপ ইনস্টল ও সাইনআপ করালেই প্রতিটি সফল ইনস্টলে ৮০৳ নিশ্চিত CPA পেআউট!",
          bonusDetails = "আপনার ইউনিক রেফারেল লিংক দিয়ে অ্যাপ ডাউনলোড ও ওটিপি ভেরিফিকেশন সম্পন্ন হলে স্বয়ংক্রিয়ভাবে কমিশন যুক্ত হবে।",
          promoCode = "APP80",
          validUntil = "চলতি মাস ব্যাপী",
          minOrdersRequired = 1,
          bonusAmount = 80.0,
          categoryTag = "CPA অ্যাপ ইনস্টল",
          offerType = "APP_INSTALL",
          targetCountry = "বাংলাদেশ",
          currency = "BDT",
          payoutRate = 80.0,
          trackingUrl = "https://app.standardbusiness.com/install?ref=REF-SB01",
          rules = "ডিভাইসে পূর্বে অ্যাপ ইনস্টল না থাকা শর্ত। এক ডিভাইসে একবার গ্রহণযোগ্য।"
        ),
        // CPA Offer 2: Merchant & Agent Referral
        OfferEntity(
          title = "বিকাশ ও ডিজিটাল মার্চেন্ট অনবোর্ডিং লিড",
          subtitle = "দোকানদার ও উদ্যোক্তাদের মার্চেন্ট অ্যাকাউন্ট তৈরিতে রেফার করলেই ১২০৳ ইনস্ট্যান্ট CPA কমিশন।",
          bonusDetails = "মার্চেন্টের ট্রেড লাইসেন্স বা জাতীয় পরিচয়পত্র ও ফোন নম্বর এন্ট্রি করে লিড জমা দিন।",
          promoCode = "MERCHANT120",
          validUntil = "৩০ দিন কার্যকর",
          minOrdersRequired = 1,
          bonusAmount = 120.0,
          categoryTag = "CPA লিড জেনারেশন",
          offerType = "CPA_LEAD",
          targetCountry = "বাংলাদেশ",
          currency = "BDT",
          payoutRate = 120.0,
          trackingUrl = "https://lead.standardbusiness.com/merchant?ref=REF-SB01",
          rules = "আমাদের কল সেন্টার থেকে ফোন করে তথ্য যাচাইয়ের ২৪ ঘণ্টার মধ্যে পেআউট অনুমোদন করা হবে।"
        ),
        // CPA Offer 3: International Global Signup
        OfferEntity(
          title = "গ্লোবাল ফ্রিল্যান্সার ও ই-কমার্স অ্যাকাউন্ট সাইনআপ (International)",
          subtitle = "আন্তর্জাতিক ক্লায়েন্টদের জন্য গ্লোবাল পেমেন্ট ও ফ্রিল্যান্সার ওয়ালেট সাইনআপে $৪.৫০ (৳৫৪০) কমিশন!",
          bonusDetails = "আন্তর্জাতিক ব্যবহারকারী বা প্রবাসী বাংলাদেশিদের কাছে অফারটি প্রমোট করুন। ইউএসডি অথবা টাকায় পেআউট পাবেন।",
          promoCode = "GLOBAL540",
          validUntil = "আন্তর্জাতিক লাইভ অফার",
          minOrdersRequired = 1,
          bonusAmount = 540.0,
          categoryTag = "গ্লোবাল CPA অফার",
          offerType = "SIGNUP",
          targetCountry = "Global / আন্তর্জাতিক",
          currency = "USD",
          payoutRate = 4.50,
          trackingUrl = "https://global.standardbusiness.com/signup?aff=REF-SB01",
          rules = "ভেরিফায়েড ইমেইল ও আইডি ভেরিফিকেশন সাপেক্ষে আন্তর্জাতিক কমিশন অনুমোদিত হয়।"
        ),
        // Promo Bonus Offer
        OfferEntity(
          title = "বৈশাখী স্পেশাল ডাবল কমিশন উৎসব",
          subtitle = "সকল শাড়ি ও থ্রি-পিসে অতিরিক্ত ১০০ টাকা বোনাস কমিশন ও ৫০ এক্সট্রা রিওয়ার্ড পয়েন্ট!",
          bonusDetails = "প্রতিটি জামদানি ও সুতি কাতান শাড়িতে রেগুলার কমিশনের সাথে পাবেন ইনস্ট্যান্ট ১০০৳ বোনাস।",
          promoCode = "BOISHAKHI26",
          validUntil = "৩০ বৈশাখ পর্যন্ত",
          minOrdersRequired = 1,
          bonusAmount = 100.0,
          categoryTag = "উৎসব ক্যাম্পেইন",
          offerType = "PROMO_BONUS"
        )
      )
      offerDao.insertAll(sampleOffers)
    }

    // Seed sample CPA conversions
    val conversionList = listOf(
      CpaConversionEntity(
        id = 1,
        offerId = 1,
        offerTitle = "শপিং অ্যাপ ইনস্টল ও অ্যাকাউন্ট সাইনআপ",
        agentId = 1,
        agentName = "সুমাইয়া ইসলাম",
        customerName = "তানভীর আহমেদ",
        customerPhone = "01722334455",
        customerEmail = "tanvir.bd@gmail.com",
        targetCountry = "বাংলাদেশ",
        payoutAmount = 80.0,
        currency = "BDT",
        status = "APPROVED",
        antiFraudHash = "1_01722334455",
        submissionDate = System.currentTimeMillis() - 86400000L,
        processedDate = System.currentTimeMillis() - 3600000L,
        adminNotes = "ইনস্টলেশন ও ওটিপি ভেরিফিকেশন শতভাগ সফল।"
      ),
      CpaConversionEntity(
        id = 2,
        offerId = 2,
        offerTitle = "বিকাশ ও ডিজিটাল মার্চেন্ট অনবোর্ডিং লিড",
        agentId = 1,
        agentName = "সুমাইয়া ইসলাম",
        customerName = "হাসান মাহমুদ (ভাই ভাই স্টোর)",
        customerPhone = "01844556677",
        customerEmail = "",
        targetCountry = "বাংলাদেশ",
        payoutAmount = 120.0,
        currency = "BDT",
        status = "PENDING",
        antiFraudHash = "2_01844556677",
        submissionDate = System.currentTimeMillis() - 7200000L,
        processedDate = null,
        adminNotes = "কল সেন্টারে ভেরিফিকেশনের জন্য লাইনে আছে।"
      ),
      CpaConversionEntity(
        id = 3,
        offerId = 3,
        offerTitle = "গ্লোবাল ফ্রিল্যান্সার ও ই-কমার্স অ্যাকাউন্ট সাইনআপ",
        agentId = 1,
        agentName = "সুমাইয়া ইসলাম",
        customerName = "Michael Robert",
        customerPhone = "+447911123456",
        customerEmail = "m.robert@cloudtech.co.uk",
        targetCountry = "Global / আন্তর্জাতিক",
        payoutAmount = 540.0,
        currency = "USD",
        status = "PENDING",
        antiFraudHash = "3_+447911123456",
        submissionDate = System.currentTimeMillis() - 14400000L,
        processedDate = null,
        adminNotes = "আন্তর্জাতিক কেওয়াইসি পেন্ডিং।"
      )
    )
    cpaConversionDao.insertConversion(conversionList[0])
    cpaConversionDao.insertConversion(conversionList[1])
    cpaConversionDao.insertConversion(conversionList[2])

    // Seed Audit Logs
    val sampleAudit = listOf(
      AuditLogEntity(
        adminId = 99,
        adminName = "Standard Business পরিচালক",
        targetAgentId = 1,
        targetAgentName = "সুমাইয়া ইসলাম",
        actionType = "BONUS_ADD",
        amount = 100.0,
        previousBalance = 2350.0,
        newBalance = 2450.0,
        reason = "বৈশাখী ক্যাম্পেইনে সেরা পারফরম্যান্স স্পেশাল বোনাস",
        timestamp = System.currentTimeMillis() - 43200000L
      ),
      AuditLogEntity(
        adminId = 99,
        adminName = "Standard Business পরিচালক",
        targetAgentId = 1,
        targetAgentName = "সুমাইয়া ইসলাম",
        actionType = "CPA_APPROVAL",
        amount = 80.0,
        previousBalance = 2270.0,
        newBalance = 2350.0,
        reason = "CPA লিড #১ অনুমোদন (শপিং অ্যাপ ইনস্টল)",
        timestamp = System.currentTimeMillis() - 86400000L
      )
    )
    auditLogDao.insertAuditLog(sampleAudit[0])
    auditLogDao.insertAuditLog(sampleAudit[1])

    if (trainingDao.count() == 0) {
      val sampleModules = listOf(
        TrainingModuleEntity(
          id = 1,
          title = "মডিউল ১: প্রোডাক্ট পরিচিতি ও ফেব্রিক কোয়ালিটি গাইড",
          subtitle = "শাড়ি, থ্রি-পিস ও জুয়েলারির আসল কোয়ালিটি চেনার উপায়",
          category = "প্রোডাক্ট নলেজ",
          durationText = "৪ মিনিট ৪০ সেকেন্ড (ভিডিও ও চিটশিট)",
          summary = "কাস্টমারদের কাছে আত্মবিশ্বাসের সাথে বিক্রি করতে প্রতিটি শাড়ির সুতা, জামদানি বুনন, থ্রি-পিসের কাপড় ও জুয়েলারির মাইক্রোপ্লেটিং সম্পর্কে সঠিক ধারণা অর্জন করুন।",
          detailedContent = "১. জামদানি ও তাঁত শাড়ির ফেব্রিক যাচাই:\nআমাদের Standard Business-এর জামদানি শাড়িগুলো ১০০% প্রিমিয়াম সুতি ও রেশমি সুতায় তৈরি। আচল ও জমিনে সোনালী জরি কাজ থাকে যা সহজে উঠে যায় না। কাস্টমারদের বলুন এটি ওজনে হালকা, নরম ও যেকোনো উৎসব-অনুষ্ঠানের উপযোগী।\n\n২. লন ও ভয়েল থ্রি-পিস:\nকামিজের বডি ৫২ ইঞ্চি পর্যন্ত কাটা সম্ভব এবং ওড়না পিওর সিফন ৫ হাত সম্পূর্ণ। ১০০% পাকা রঙের গ্যারান্টি থাকায় প্রথম ধোয়াতেই কোনো রঙ ফেড হবে না।\n\n৩. গোল্ড প্লেটেড জুয়েলারি:\nব্রাস মেটালের উপর মাইক্রোপ্লেটিং করা। পানি বা পারফিউম সরাসরি স্প্রে না করলে ৩ থেকে ৫ বছর পর্যন্ত নিখুঁত গ্লেজ থাকে।",
          keyTakeaways = "• সুতি মসলিন জামদানির আচল ১০০% খাঁটি সুতা ও জরি কাজে বোনা\n• থ্রি-পিস আনস্টিচড এবং যেকোনো সাইজে সেলাই করার উপযোগী\n• কুন্দন জুয়েলারিতে অ্যান্টি-টার্নিশ কোটিং দেওয়া থাকে",
          practicalTips = "💡 টিপস: কাস্টমারকে ছবি পাঠানোর পাশাপাশি ফেব্রিকের ক্লোজ-আপ ভিডিও পাঠালে অর্ডার কনফার্মেশন হার ৬০% বেড়ে যায়।",
          videoTitle = "শাড়ি, ড্রেস ও জুয়েলারির মেটেরিয়াল ও কোয়ালিটি ডেমো",
          isCompleted = true,
          isBookmarked = true,
          moduleOrder = 1
        ),
        TrainingModuleEntity(
          id = 2,
          title = "মডিউল ২: ফেসবুক ও সোশ্যাল মিডিয়া সেলস ক্লোজিং টেকনিক",
          subtitle = "ফেসবুক লাইভ ও হোয়াটসঅ্যাপে কাস্টমার আকর্ষণ ও সেলস ক্লোজিং",
          category = "মার্কেটিং কৌশল",
          durationText = "৬ মিনিট ২০ সেকেন্ড (ভিডিও ও চ্যাট স্ক্রিপ্ট)",
          summary = "কীভাবে ফেসবুক পেজে অর্গানিক ফলোয়ার বাড়াবেন, ফেসবুক লাইভে অর্ডার নেবেন এবং মেসেঞ্জারে কাস্টমারকে সন্তুষ্ট করে দ্রুত ক্যাশ অন ডেলিভারি অর্ডার কনফার্ম করবেন।",
          detailedContent = "১. ফেসবুক পেজ ব্র্যান্ডিং:\nআপনার পেজের নাম একটি আকর্ষণীয় নামে রাখুন। যেমন: 'সুমাইয়া ক্লথিং কর্নার (Standard Business Verified Partner)'.\n\n২. আকর্ষণীয় পোস্ট ফরম্যাট:\nশুধু ছবি পোস্ট করবেন না। আমাদের অ্যাপের 'মার্কেটিং করো' বাটনে ট্যাপ করে প্রস্তুত বাংলা ক্যাপশন ও আকর্ষণীয় মূল্য কপি করে পেস্ট করুন।\n\n৩. মেসেঞ্জারে সেলস ক্লোজিং ম্যাজিক টেকনিক:\nকাস্টমার দাম জিজ্ঞেস করলে শুধু '৳১৬৫০' বলবেন না। এভাবে লিখুন:\n'আপু/ভাইয়া, এই কাতান জামদানিটি আমাদের বেস্ট সেলিং কালেকশন। আজই অর্ডার করলে সারাদেশে ক্যাশ অন ডেলিভারি এবং পার্সেল খুলে দেখে মূল্য পরিশোধের সুবিধা পাবেন। আপনার নাম ও ঠিকানা দিলে আজই বুকিং করে দেব!'\n\n৪. হোয়াটসঅ্যাপ ব্রডকাস্ট গ্রুপ:\nযারা একবার কেনাকাটা করেছে তাদের একটি VIP হোয়াটসঅ্যাপ গ্রুপে রাখুন এবং প্রতি সপ্তাহে নতুন স্টক এলে সবার আগে তাদেরকে অফার দিন।",
          keyTakeaways = "• প্রতিদিন নির্দিষ্ট সময়ে (রাত ৮টা-১০টা) ফেসবুকে ২-৩টি পোস্ট দিন\n• কাস্টমারের মেসেজের উত্তর ১ মিনিটের মধ্যে দেওয়ার চেষ্টা করুন\n• 'পণ্য দেখে মূল্য পরিশোধ' বাক্যটি কাস্টমারের মধ্যে সর্বোচ্চ বিশ্বাস তৈরি করে",
          practicalTips = "💡 টিপস: পোস্টের সাথে 'স্টক সীমিত, আজই ইনবক্স করুন' লিখলে কাস্টমার দ্রুত অর্ডার কনফার্ম করতে আগ্রহী হয়।",
          videoTitle = "লাইভ সেলস ও ফেসবুক চ্যাটে অর্ডার নেওয়ার গোপন কৌশল",
          isCompleted = false,
          isBookmarked = false,
          moduleOrder = 2
        ),
        TrainingModuleEntity(
          id = 3,
          title = "মডিউল ৩: Standard Business অ্যাপ ব্যবহার, কুরিয়ার ট্র্যাকিং ও পেমেন্ট গাইড",
          subtitle = "অর্ডার প্লেস, কুরিয়ার ট্র্যাকিং এবং বিকাশ/নগদে কমিশন উত্তোলন",
          category = "অ্যাপ ও পেমেন্ট গাইড",
          durationText = "৫ মিনিট ১৫ সেকেন্ড (ভিডিও ও স্টেপ-বাই-স্টেপ)",
          summary = "Standard Business অ্যাপের প্রতিটি ফিচার নিখুঁতভাবে ব্যবহার করার সম্পূর্ণ গাইড — প্রফিট মার্জিন সেট করা, কাস্টমার অ্যাড্রেস এন্ট্রি, কুরিয়ার ট্র্যাকিং এবং বিকাশ/নগদ ওয়ালেট উইথড্রয়াল।",
          detailedContent = "১. প্রফিট মার্জিন ও কমিশন ক্যালকুলেশন:\nপ্রতিটি প্রোডাক্টে কোম্পানির নির্ধারিত পাইকারি মূল্য থাকে এবং একটি নিশ্চিত বেস কমিশন থাকে (যেমন: ৳২৫০)। আপনি নিজের ইচ্ছামতো বাড়তি মার্জিন যোগ করে কাস্টমার প্রাইস সেট করতে পারেন। আপনার মোট প্রফিট = (কাস্টমার প্রাইস - পাইকারি রেট) + বেস কমিশন।\n\n২. অর্ডার প্লেসমেন্ট ও ডেলিভারি চার্জ:\nঢাকা সিটির ভেতরে ডেলিভারি চার্জ মাত্র ৬০৳ এবং ঢাকার বাইরে ১২০৳। কাস্টমারের সম্পূর্ণ নাম, সচল মোবাইল নম্বর এবং জেলা নির্বাচন করে অর্ডার কনফার্ম করুন।\n\n৩. কুরিয়ার হ্যান্ডওভার ও লাইভ ট্র্যাকিং:\nঅর্ডার দেওয়ার পর আমাদের সেন্ট্রাল টিম ২ ঘণ্টার মধ্যে প্যাকিং করে Steadfast বা Pathao কুরিয়ারে বুকিং দেয়। আপনি অ্যাপের 'ট্র্যাকিং' ট্যাবে ট্র্যাকিং কোড দিয়ে রিয়েল-টাইম কুরিয়ার আপডেট দেখতে পাবেন।\n\n৪. ডেলিভারি সম্পন্ন ও ইনস্ট্যান্ট কমিশন ক্রেডিট:\nকুরিয়ার পার্সেল ডেলিভারি করে টাকা কালেক্ট করার সাথে সাথেই আপনার পেন্ডিং কমিশন ওয়ালেটের 'উইথড্র যোগ্য ব্যালেন্স'-এ যোগ হবে।\n\n৫. বিকাশ ও নগদ উইথড্রয়াল:\nন্যূনতম ১০০ টাকা হলেই যে কোনো সময় সরাসরি আপনার বিকাশ বা নগদ পার্সোনাল নম্বরে উইথড্রয়াল রিকোয়েস্ট পাঠান। ২ ঘণ্টার মধ্যে টাকা পৌঁছে যাবে।",
          keyTakeaways = "• ঢাকার ভেতরে ৬০৳, বাইরে ১২০৳ ডেলিভারি চার্জ\n• পার্সেল ডেলিভারি হওয়ার সাথে সাথেই কমিশন আনলক হয়\n• ১০০৳ থেকে শুরু করে যেকোনো ব্যালেন্স সরাসরি বিকাশ/নগদে ক্যাশআউট করা যায়",
          practicalTips = "💡 টিপস: কাস্টমারকে অর্ডারের পর কুরিয়ার ট্র্যাকিং কোড মেসেজ করে দিলে পার্সেল রিটার্ন হওয়ার সম্ভাবনা ৯০% কমে যায়।",
          videoTitle = "Standard Business অ্যাপে অর্ডার এন্ট্রি থেকে ওয়ালেট উইথড্রয়াল লাইভ ডেমো",
          isCompleted = false,
          isBookmarked = false,
          moduleOrder = 3
        )
      )
      trainingDao.insertAll(sampleModules)
    }

    notificationDao.insertAll(
      listOf(
        NotificationEntity(
          title = "অর্ডার ডেলিভারি সফল! 🎉",
          body = "আপনার অর্ডার #SB-2026-101 এর ডেলিভারি সফল হয়েছে এবং ৳৫০০ কমিশন ওয়ালেটে যুক্ত হয়েছে।",
          type = "COMMISSION",
          timestamp = System.currentTimeMillis() - 7200000L,
          isRead = false
        ),
        NotificationEntity(
          title = "নতুন হট প্রোডাক্ট ও CPA ক্যাম্পেইন চালু 🔥",
          body = "ফ্যাশন, গ্রোসারি ও গ্যাজেটের নতুন কালেকশন এসেছে। পাশাপাশি CPA অফারে প্রতি ইনস্টলে ৮০৳ ইনকাম করুন!",
          type = "PRODUCT",
          timestamp = System.currentTimeMillis() - 18000000L,
          isRead = false
        ),
        NotificationEntity(
          title = "বিকাশ পেআউট সম্পন্ন 💰",
          body = "আপনার ৳৮০০ উইথড্রয়াল সফলভাবে বিকাশ একাউন্টে পাঠানো হয়েছে (TrxID: NG7721903)।",
          type = "PAYOUT",
          timestamp = System.currentTimeMillis() - 86400000L,
          isRead = true
        )
      )
    )
  }
}
