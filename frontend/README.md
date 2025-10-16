
  # Notion-Style Finance Platform

  This is a code bundle for Notion-Style Finance Platform. The original project is available at https://www.figma.com/design/5ISYjorAP7YOv7UitZrGvS/Notion-Style-Finance-Platform.

  ## Running the code

  Run `npm i` to install the dependencies.

Run `npm run dev` to start the development server.

## Application structure

The frontend now uses [React Router](https://reactrouter.com/) for client-side routing:

- `/` renders the **Dashboard** with the existing transaction filters and table.
- `/audit-log` renders the **Audit Log** page, which fetches data from the audit-log service and provides search and filtering controls.
  