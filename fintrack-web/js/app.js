// Configuration for API endpoints
const API_BASE_URL = 'http://localhost:8080';
const TRANSACTIONS_API = `${API_BASE_URL}/api/transaction`;

// Global state variables
let allTransactions = [];
let categories = new Set();
let transactionToDelete = null;

// Initialize application when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
  loadTransactions();
  setupEventListeners();
});

// Load all transactions from the backend
async function loadTransactions() {
  showLoading(true);

  try {
    const response = await fetch(TRANSACTIONS_API);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    allTransactions = await response.json();
    updateCategories();
    renderTransactions(allTransactions);
    updateStatistics(allTransactions);

    showLoading(false);

    // Show "no data" message if no transactions
    if (allTransactions.length === 0) {
      showNoData(true);
    }
  } catch (error) {
    console.error('Error loading transactions:', error);
    showNotification('Error loading data', 'error');
    showLoading(false);
    showNoData(true);
  }
}

// Display transactions in the table
function renderTransactions(transactions) {
  const tbody = document.getElementById('transactionsTable');
  tbody.innerHTML = '';

  if (transactions.length === 0) {
    showNoData(true);
    return;
  }

  showNoData(false);

  transactions.forEach(transaction => {
    const row = document.createElement('tr');

    // Format currency and date for display
    const formattedAmount = formatCurrency(transaction.amount);
    const formattedDate = formatDateTime(transaction.dateTime);
    const category = transaction.category || '-';

    // Create table row HTML
    row.innerHTML = `
    <td class="amount">${formattedAmount}</td>
    <td>${category}</td>
    <td>${formattedDate}</td>
    <td>
        <button class="btn btn-action btn-edit" onclick="editTransaction(${transaction.id ?? ''})">
            <i class="fas fa-edit"></i>
        </button>
        <button class="btn btn-action btn-delete" onclick="showDeleteModal(${transaction.id ?? ''})">
            <i class="fas fa-trash"></i>
        </button>
    </td>
`;
    tbody.appendChild(row);
  });
}

// Calculate and display statistics
function updateStatistics(transactions) {
  if (transactions.length === 0) {
    // Set default values when no transactions
    document.getElementById('totalAmount').textContent = '0 ₽';
    document.getElementById('totalCount').textContent = '0';
    document.getElementById('averageAmount').textContent = '0 ₽';
    document.getElementById('lastAdded').textContent = '-';
    return;
  }

  // Calculate total amount
  const totalAmount = transactions.reduce((sum, t) => sum + parseFloat(t.amount), 0);
  const averageAmount = totalAmount / transactions.length;

  // Find most recent transaction by date
  const recent = transactions.sort((a, b) =>
    new Date(b.createdAt || b.created_at) - new Date(a.createdAt || a.created_at)
  )[0];
  const recentDate = formatDateTime(recent.createdAt || recent.created_at);

  // Update statistics display
  document.getElementById('totalAmount').textContent = formatCurrency(totalAmount);
  document.getElementById('totalCount').textContent = transactions.length;
  document.getElementById('averageAmount').textContent = formatCurrency(averageAmount);
  document.getElementById('lastAdded').textContent = recentDate;
}

// Update categories list from existing transactions
function updateCategories() {
  categories.clear();

  // Collect unique categories
  allTransactions.forEach(t => {
    if (t.category) {
      categories.add(t.category);
    }
  });

  // Update datalist for autocomplete
  const datalist = document.getElementById('categories');
  datalist.innerHTML = '';

  categories.forEach(category => {
    const option = document.createElement('option');
    option.value = category;
    datalist.appendChild(option);
  });

  // Update category filter dropdown
  const categoryFilter = document.getElementById('categoryFilter');
  const currentValue = categoryFilter.value;

  categoryFilter.innerHTML = '<option value="">All categories</option>';
  categories.forEach(category => {
    const option = document.createElement('option');
    option.value = category;
    option.textContent = category;
    categoryFilter.appendChild(option);
  });

  // Restore previous filter selection if it still exists
  if (currentValue && categories.has(currentValue)) {
    categoryFilter.value = currentValue;
  }
}

// Filter transactions based on search criteria
function filterTransactions() {
  const searchTerm = document.getElementById('searchInput').value.toLowerCase();
  const categoryFilter = document.getElementById('categoryFilter').value;
  const minAmount = parseFloat(document.getElementById('minAmount').value);
  const maxAmount = parseFloat(document.getElementById('maxAmount').value);
  const dateFrom = document.getElementById('dateFrom').value;
  const dateTo = document.getElementById('dateTo').value;

  const filtered = allTransactions.filter(transaction => {
    // Filter by search term (category only)
    if (searchTerm && transaction.category &&
      !transaction.category.toLowerCase().includes(searchTerm)) {
      return false;
    }

    // Filter by category selection
    if (categoryFilter && transaction.category !== categoryFilter) {
      return false;
    }

    // Filter by minimum amount
    if (!isNaN(minAmount) && transaction.amount < minAmount) {
      return false;
    }

    // Filter by maximum amount
    if (!isNaN(maxAmount) && transaction.amount > maxAmount) {
      return false;
    }

    // Filter by date range
    const transactionDate = new Date(transaction.createdAt || transaction.created_at);

    if (dateFrom && transactionDate < new Date(dateFrom)) {
      return false;
    }

    if (dateTo) {
      const toDate = new Date(dateTo);
      toDate.setHours(23, 59, 59, 999);
      if (transactionDate > toDate) {
        return false;
      }
    }

    return true;
  });

  renderTransactions(filtered);
  updateStatistics(filtered);
}

// Open modal for creating new transaction
function openCreateModal() {
  document.getElementById('modalTitle').textContent = 'New Transaction';
  document.getElementById('transactionForm').reset();
  document.getElementById('transactionId').value = '';

  const modal = document.getElementById('modal');
  modal.classList.remove('hidden');

  // Focus on amount input
  document.getElementById('amount').focus();
}

// Open modal for editing existing transaction
async function editTransaction(id) {
  try {
    const response = await fetch(`${TRANSACTIONS_API}/${id}`);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const transaction = await response.json();

    document.getElementById('modalTitle').textContent = 'Edit Transaction';
    document.getElementById('transactionId').value = transaction.transactionId;
    document.getElementById('amount').value = transaction.amount;
    document.getElementById('category').value = transaction.category || '';

    const modal = document.getElementById('modal');
    modal.classList.remove('hidden');
    document.getElementById('amount').focus();
  } catch (error) {
    console.error('Error loading transaction:', error);
    showNotification('Error loading transaction', 'error');
  }
}

// Close the create/edit modal
function closeModal() {
  document.getElementById('modal').classList.add('hidden');
}

// Handle form submission for create/edit
async function handleSubmit(event) {
  event.preventDefault();

  // Debug logging
  console.log("=== DEBUG: Form submission started ===");

  // Get form values
  const amountInput = document.getElementById('amount');
  const amountValue = amountInput.value.trim();
  const transactionId = document.getElementById('transactionId').value;
  const isEdit = !!transactionId;

  // Validate amount field (required)
  if (!amountValue) {
    console.error("Amount field is empty");
    showNotification("Amount field is required", "error");
    amountInput.focus();
    return;
  }

  // Parse amount as float
  const amount = parseFloat(amountValue);
  if (isNaN(amount)) {
    console.error("Invalid amount value:", amountValue);
    showNotification("Please enter a valid number for amount", "error");
    amountInput.focus();
    return;
  }

  // Prepare transaction data
  const transactionData = {
    amount: amount,
    category: document.getElementById('category').value.trim() || null
  };

  console.log("Sending transaction data:", transactionData);

  try {
    let response;

    if (isEdit) {
      // Update existing transaction
      console.log("Updating transaction ID:", transactionId);
      response = await fetch(`${TRANSACTIONS_API}/${transactionId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          ...transactionData,
          transactionId: parseInt(transactionId)
        })
      });
    } else {
      // Create new transaction
      console.log("Creating new transaction");
      response = await fetch(TRANSACTIONS_API, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(transactionData)
      });
    }

    console.log("Response status:", response.status);

    const responseText = await response.text();
    console.log("Response text:", responseText);

    if (!response.ok) {
      throw new Error(responseText || `HTTP error! status: ${response.status}`);
    }

    // Show success message
    if (isEdit) {
      showNotification('Transaction updated successfully', 'success');
    } else {
      showNotification('Transaction created successfully', 'success');
    }

    // Close modal and reload data
    closeModal();
    await loadTransactions();

  } catch (error) {
    console.error('Error saving transaction:', error);
    showNotification(`Error: ${error.message}`, 'error');
  }
}

// Show delete confirmation modal
function showDeleteModal(id) {
  const transaction = allTransactions.find(t => t.transactionId === id);

  if (!transaction) {
    showNotification('Transaction not found', 'error');
    return;
  }

  transactionToDelete = id;
  document.getElementById('deleteAmount').textContent = formatCurrency(transaction.amount);
  document.getElementById('deleteCategory').textContent = transaction.category || '-';

  const modal = document.getElementById('deleteModal');
  modal.classList.remove('hidden');
}

// Close delete confirmation modal
function closeDeleteModal() {
  transactionToDelete = null;
  document.getElementById('deleteModal').classList.add('hidden');
}

// Confirm and execute deletion
async function confirmDelete() {
  if (!transactionToDelete) {
    return;
  }

  try {
    const response = await fetch(`${TRANSACTIONS_API}/${transactionToDelete}`, {
      method: 'DELETE'
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    showNotification('Transaction deleted successfully', 'success');
    closeDeleteModal();
    await loadTransactions();

  } catch (error) {
    console.error('Error deleting transaction:', error);
    showNotification('Error deleting transaction', 'error');
  }
}

// Helper function: Format currency with Russian Ruble symbol
function formatCurrency(amount) {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'RUB',
    minimumFractionDigits: 2
  }).format(amount);
}

// Helper function: Format date and time for display
function formatDateTime(dateString) {
  if (!dateString) return '-';

  const date = new Date(dateString);
  return date.toLocaleDateString('en-GB', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
}

// Show/hide loading indicator
function showLoading(show) {
  const loading = document.getElementById('loading');
  const noData = document.getElementById('noData');

  if (show) {
    loading.classList.remove('hidden');
    noData.classList.add('hidden');
  } else {
    loading.classList.add('hidden');
  }
}

// Show/hide "no data" message
function showNoData(show) {
  const noData = document.getElementById('noData');
  const tbody = document.getElementById('transactionsTable');

  if (show) {
    noData.classList.remove('hidden');
    tbody.innerHTML = '';
  } else {
    noData.classList.add('hidden');
  }
}

// Display notification message
function showNotification(message, type) {
  const notification = document.getElementById('notification');

  notification.textContent = message;
  notification.className = `notification ${type}`;
  notification.classList.remove('hidden');

  // Auto-hide notification after 5 seconds
  setTimeout(() => {
    notification.classList.add('hidden');
  }, 5000);
}

// Set up event listeners for modals
function setupEventListeners() {
  // Close modal when clicking outside
  document.getElementById('modal').addEventListener('click', function(event) {
    if (event.target === this) {
      closeModal();
    }
  });

  // Close delete modal when clicking outside
  document.getElementById('deleteModal').addEventListener('click', function(event) {
    if (event.target === this) {
      closeDeleteModal();
    }
  });

  // Close modals with Escape key
  document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
      closeModal();
      closeDeleteModal();
    }
  });
}

// Export functions for use in HTML onclick handlers
window.openCreateModal = openCreateModal;
window.closeModal = closeModal;
window.handleSubmit = handleSubmit;
window.editTransaction = editTransaction;
window.showDeleteModal = showDeleteModal;
window.closeDeleteModal = closeDeleteModal;
window.confirmDelete = confirmDelete;
window.filterTransactions = filterTransactions;
