DATABASE_HOST='localhost'\
DATABASE_NAME='acehq'\
DATABASE_USERNAME='postgres'\
DATABASE_PASSWORD='postgres'\
CLERK_SECRET_KEY='{yourSecretKey}'
CLERK_AUTHORIZED_PARTY='http://localhost:3000'
FRONTEND_URL=http://localhost:3000
BACKEND_URL=http://localhost:8080

# Generate Long-lived Clerk JWT Token
Run this in the frontend's console:
```js
await window.Clerk.session.getToken({ template: 'Testing-JWT' })
```