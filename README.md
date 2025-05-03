# 🔐 Secure File Vault

> A modern, encrypted cloud storage vault for secure file management — crafted end-to-end with security, performance, and elegance in mind.

---

## 📌 Overview

Secure File Vault is a production-ready, privacy-first cloud storage application built entirely from scratch. It combines modern encryption (AES-256), a polished UI, and real-time cloud interaction — fully implemented by **Harsha Vardhan Dasari**, a passionate backend developer focused on secure systems.

This project reflects a strong emphasis on:

* **Cybersecurity-first** design
* **Scalability** using Spring Boot & MongoDB Atlas
* **Cloud-native** encryption + storage

---

## 💼 Features

| Feature                 | Status       |
| ----------------------- | ------------ |
| AES-256 File Encryption | ✅ Complete   |
| SHA-256 User Login      | ✅ Complete   |
| Upload & Cloud Save     | ✅ Cloudinary |
| Download + Auto Decrypt | ✅ Complete   |
| Delete Securely         | ✅ Complete   |
| Folder-based UI         | ✅ Integrated |
| File Metadata Display   | ✅ Done       |
| Toast Feedback UI       | ✅ Integrated |
| User Dashboard          | ✅ Complete   |
| Elegant Tailwind UI     | ✅ Complete   |
| Deployment Ready        | 🚀 Ready     |
| Mark as Favorite        | ✅ Complete   |
| Full-text File Search   | ✅ Complete   |

---

## 🛠 Tech Stack

* **Backend**: Java + Spring Boot
* **Database**: MongoDB Atlas
* **Encryption**: AES-256 (custom), SHA-256 (login)
* **Cloud Storage**: Cloudinary API (raw encrypted files)
* **Frontend**: TailwindCSS + Bootstrap + HTML5

---

## 📷 UI Preview

Coming soon: screenshots & feature GIFs.

---

## 🚀 Deployment

### GitHub → Render Steps

1. Push your full Spring Boot project to a **GitHub repository**
2. Sign in to **[Render](https://render.com/)**
3. Click **"New Web Service"** → Connect your GitHub → Select the repo
4. Set build and start commands:

   * **Build Command**: `./gradlew build`
   * **Start Command**: `java -jar build/libs/secure-vault-0.0.1-SNAPSHOT.jar`
5. Add environment variables:

   * `MONGODB_URI` — your MongoDB Atlas connection string
   * `CLOUDINARY_URL` — Cloudinary API key format
6. Deploy — Render will build & serve your app online

✅ Done! Your Secure File Vault is now live and public.

---

## 👨‍💻 Author Note

> Hello! I'm **Harsha Vardhan Dasari**, a backend developer with a focus on security, real-world architecture, and clean API-driven systems.

This project is a pure from-scratch implementation — no generators, no templates — only handwritten logic, encryption, and APIs.

Feel free to fork, learn from, or build upon this!

---

## 📎 License

MIT License (2025) — Free to use with attribution.

---

## 🌐 Links

* 🔗 GitHub Repo: [github.com/Harhsa](https://github.com/Harhsa)
* 🔴 Live Demo (Render): [secure-file-vault.onrender.com](https://secure-file-vault.onrender.com) *(after deployment)*
